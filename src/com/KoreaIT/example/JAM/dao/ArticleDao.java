package com.KoreaIT.example.JAM.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class ArticleDao {

	private Connection conn;
	
	public ArticleDao(Connection conn) {
		this.conn = conn;
	}

	public int doWrite(String title, String body, int loginedMemberId) {
		
		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		
		return DBUtil.insert(conn, sql);
	}
	
	public List<Map<String, Object>> getArticles(String searchKeyword){
		SecSql sql = new SecSql();

		sql.append("SELECT a.*, m.name AS writerName");
		sql.append("FROM article AS a");
		sql.append("INNER JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		if(searchKeyword.length() > 0) {
			sql.append("WHERE a.title LIKE CONCAT('%', ? , '%')", searchKeyword);
		}
		sql.append("ORDER BY a.id DESC");
		
		return DBUtil.selectRows(conn, sql);
	}

	public Map<String, Object> getArticle(int id) {
		SecSql sql = new SecSql();

		sql.append("SELECT a.*, m.name AS writerName");
		sql.append("FROM article AS a");
		sql.append("INNER JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("WHERE a.id = ?", id);
		
		return DBUtil.selectRow(conn, sql);
	}

	public int getArticleCount(int id) {
		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		
		return DBUtil.selectRowIntValue(conn, sql);
	}

	public void doModify(String title, String body, int id) {
		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append("WHERE id = ?", id);
		
		DBUtil.update(conn, sql);
	}

	public void doDelete(int id) {
		SecSql sql = new SecSql();

		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);
		
		DBUtil.delete(conn, sql);
	}

	public int increaseVCnt(int id) {
		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET vCnt = vCnt + 1");
		sql.append("WHERE id = ?", id);
		
		return DBUtil.update(conn, sql);
	}

}
