package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class App {
	public void run() {
		Scanner sc = new Scanner(System.in);
		System.out.println("== 프로그램 시작 ==");

		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("article write")) {
					System.out.println("== 게시물 작성 ==");

					System.out.printf("제목 : ");
					String title = sc.nextLine();
					System.out.printf("내용 : ");
					String body = sc.nextLine();

					SecSql sql = new SecSql();
					
					sql.append("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					
					int id = DBUtil.insert(conn, sql);
					
					System.out.printf("%d번 글이 생성되었습니다\n", id);
				} else if (cmd.equals("article list")) {
					System.out.println("== 게시물 목록 ==");

					List<Article> articles = new ArrayList<>();

					SecSql sql = new SecSql();
					
					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("ORDER BY id DESC");
					
					List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
					
					for(Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}
					
					if (articles.size() == 0) {
						System.out.println("게시물이 없습니다");
						continue;
					}

					System.out.println("번호	|	제목");

					for (Article article : articles) {
						System.out.printf("%d	|	%s\n", article.id, article.title);
					}
				} else if (cmd.startsWith("article modify ")) {
					System.out.println("== 게시물 수정 ==");
					int id = Integer.parseInt(cmd.split(" ")[2]);

					System.out.printf("수정할 제목 : ");
					String title = sc.nextLine();
					System.out.printf("수정할 내용 : ");
					String body = sc.nextLine();

//					try {
//						String sql = "UPDATE article";
//						sql += " SET updateDate = NOW()";
//						sql += ", title = '" + title + "'";
//						sql += ", `body` = '" + body + "'";
//						sql += " WHERE id = " + id + ";";
//
//						pstmt = conn.prepareStatement(sql);
//						pstmt.executeUpdate();
//					} catch (SQLException e) {
//						System.out.println("에러: " + e);
//					}

					System.out.printf("%d번 글이 수정되었습니다\n", id);
				}

				if (cmd.equals("exit")) {
					System.out.println("== 프로그램 종료 ==");
					break;
				}

			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sc.close();
	}
}
