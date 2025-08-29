package test.servlet;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IntegrationTest {

	private static final String BASE = "http://localhost:8080/ProductManage";
	private static HttpClient client;

	@BeforeAll
	static void setUp() {
		var cookieManager = new java.net.CookieManager();
		cookieManager.setCookiePolicy(java.net.CookiePolicy.ACCEPT_ALL);
		client = HttpClient.newBuilder()
				.cookieHandler(cookieManager)
				.followRedirects(Redirect.NEVER)
				.connectTimeout(Duration.ofSeconds(5))
				.build();
	}

	//ユーティリティ
	private static HttpResponse<String> postForm(String path, String form) throws Exception {
		var req = HttpRequest.newBuilder()
				.uri(URI.create(BASE + path))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString(form))
				.build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	private static HttpResponse<String> get(String path) throws Exception {
		var req = HttpRequest.newBuilder()
				.uri(URI.create(BASE + path))
				.GET().build();
		return client.send(req, HttpResponse.BodyHandlers.ofString());
	}

	private static HttpResponse<String> followIfRedirect(HttpResponse<String> res) throws Exception {
		if (List.of(301, 302, 303, 307, 308).contains(res.statusCode())) {
			String loc = res.headers().firstValue("Location").orElse("");
			if (!loc.isEmpty()) {
				URI next = loc.startsWith("http") ? URI.create(loc)
						: URI.create(BASE + (loc.startsWith("/") ? "" : "/") + loc);
				var req = HttpRequest.newBuilder().uri(next).GET().build();
				return client.send(req, HttpResponse.BodyHandlers.ofString());
			}
		}
		return res;
	}

	private static void loginAs(String user, String pass) throws Exception {
		String form = "username=" + URLEncoder.encode(user, StandardCharsets.UTF_8) +
				"&password=" + URLEncoder.encode(pass, StandardCharsets.UTF_8);
		var res = postForm("/LoginServlet", form);
		res = followIfRedirect(res);
		assertEquals(200, res.statusCode(), "ログイン後のレスポンスが200ではありません");
		assertTrue(res.body().contains("商品一覧") || res.body().toLowerCase().contains("product"),
				"ログイン後に一覧ページが表示されていない可能性");
	}

	//テスト

	@Test
	@DisplayName("IT-01: ログイン→商品一覧に遷移")
	void login_to_list() throws Exception {
		loginAs("user", "1111");
	}

	@Test
	@DisplayName("IT-02: 未ログインで一覧直アクセス→ログイン画面に戻る")
	void block_without_login() throws Exception {
		var noCookieClient = HttpClient.newBuilder().followRedirects(Redirect.NEVER).build();
		var req = HttpRequest.newBuilder()
				.uri(URI.create(BASE + "/ProductListServlet"))
				.GET().build();
		var res = noCookieClient.send(req, HttpResponse.BodyHandlers.ofString());

		boolean redirectedToLogin = List.of(302, 303).contains(res.statusCode())
				&& res.headers().firstValue("Location").orElse("").contains("login");
		boolean loginHtml = res.body().contains("ログイン") || res.body().toLowerCase().contains("login");

		assertTrue(redirectedToLogin || loginHtml, "未ログインで一覧に入れてしまっています");
	}

	@Test
	@DisplayName("IT-03: 商品登録→一覧に反映")
	void register_then_list_contains() throws Exception {
		loginAs("user", "1111");

		String newName = "結合テスト_" + System.currentTimeMillis();
		String form = "name=" + URLEncoder.encode(newName, StandardCharsets.UTF_8)
				+ "&price=1234&stock=5&categoryId=1";
		followIfRedirect(postForm("/ProductRegisterServlet", form));

		var list = get("/ProductListServlet");
		assertEquals(200, list.statusCode());
		assertTrue(list.body().contains(newName), "一覧に登録した商品が見つかりません");
	}

	@Test
	@DisplayName("IT-04: ログアウト→保護ページに入れない")
	void logout_then_block() throws Exception {
		loginAs("user", "1111");

		followIfRedirect(get("/LogoutServlet"));

		var list = get("/ProductListServlet");
		boolean redirectedToLogin = List.of(302, 303).contains(list.statusCode())
				&& list.headers().firstValue("Location").orElse("").contains("login");
		boolean loginHtml = list.body().contains("ログイン") || list.body().toLowerCase().contains("login");
		assertTrue(redirectedToLogin || loginHtml, "ログアウト後に一覧へ入れてしまっています");
	}

	@Test
	@DisplayName("IT-05: 商品削除後に一覧から消える")
	void delete_then_removed_from_list() throws Exception {
		loginAs("user", "1111");

		String name = "削除テスト_" + System.currentTimeMillis();
		String regForm = "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)
				+ "&price=999&stock=1&categoryId=1";
		followIfRedirect(postForm("/ProductRegisterServlet", regForm));

		var afterReg = get("/ProductListServlet");
		assertEquals(200, afterReg.statusCode());
		String html = afterReg.body();
		var p = java.util.regex.Pattern.compile(
				"<td>(\\d+)</td>\\s*<td>\\s*" + java.util.regex.Pattern.quote(name) + "\\s*</td>",
				java.util.regex.Pattern.DOTALL);
		var m = p.matcher(html);
		assertTrue(m.find(), "登録した商品のIDが一覧から見つかりません");
		String id = m.group(1);

		followIfRedirect(postForm("/ProductDeleteServlet", "id=" + id));

		var listRes = get("/ProductListServlet");
		assertEquals(200, listRes.statusCode());
		assertFalse(listRes.body().contains(name), "削除した商品が一覧に残っている");
	}

	@Test
	@DisplayName("IT-06: 商品編集後に一覧へ反映")
	void edit_then_reflected_in_list() throws Exception {
		loginAs("user", "1111");

		String original = "編集前_" + System.currentTimeMillis();
		String regForm = "name=" + URLEncoder.encode(original, StandardCharsets.UTF_8)
				+ "&price=1000&stock=2&categoryId=1";
		followIfRedirect(postForm("/ProductRegisterServlet", regForm));

		var afterReg = get("/ProductListServlet");
		assertEquals(200, afterReg.statusCode());
		String html = afterReg.body();
		var p = java.util.regex.Pattern.compile(
				"<td>(\\d+)</td>\\s*<td>\\s*"
						+ java.util.regex.Pattern.quote(original)
						+ "\\s*</td>",
				java.util.regex.Pattern.DOTALL);
		var m = p.matcher(html);
		assertTrue(m.find(), "登録した商品のIDが一覧から見つかりません");
		String id = m.group(1);

		String edited = "編集後_" + System.currentTimeMillis();
		String editForm = "id=" + id
				+ "&name=" + URLEncoder.encode(edited, StandardCharsets.UTF_8)
				+ "&price=2000&stock=3&categoryId=1";
		followIfRedirect(postForm("/EditProductServlet", editForm));

		var listRes = get("/ProductListServlet");
		assertEquals(200, listRes.statusCode());
		assertTrue(listRes.body().contains(edited), "編集後の名前が一覧に反映されていない");
		assertFalse(listRes.body().contains(original), "編集前の名前が残っている");
	}
}