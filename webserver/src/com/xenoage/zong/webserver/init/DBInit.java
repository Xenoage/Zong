package com.xenoage.zong.webserver.init;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Initializes an empty database by creating the
 * required tables.
 * 
 * @author Andreas Wenger
 */
public class DBInit {

	public static void initDatabase(Connection con)
		throws SQLException {
		INSTANCE.log(Companion.remark("Creating database tables..."));
		//datatype for audio formats
		sql(con, "create domain audioformat as varchar default 'OGG' check value in('OGG',	'MP3')");
		//table "docs": list of documents which are currently stored
		sql(con, "create table docs (" + "id int not null auto_increment, " + //ID of the document
			"url varchar not null, " + //URL of the document
			"public_id uuid not null, " + //public unique ID of the document, which is known by the client
			"pages int not null, " + //number of pages in this document
			"last_access int not null, " + //UNIX timestamp of last access to this document
			"primary key (id), " + "unique (url))");
		//table "audio": list of all stored audio files
		sql(con, "create table audio (" + "doc_id int not null, " + //ID of the document
			"format audioformat not null, " + //the audio format
			"audio blob not null, " + //the sampled audio file
			"primary key (doc_id, format), " + "foreign key (doc_id) references docs(id))");
		//table "cursors": List of the cursor positions for each file
		sql(con, "create table cursors (" + "doc_id int not null, " + //ID of the document (table docs)
			"cursors text not null, " + //the cursor positions
			"primary key (doc_id), " + "foreign key (doc_id) references docs(id))");
		//table "pages": list of all stored rendered pages
		sql(con, "create table pages (" + "doc_id int not null, " + //ID of the document (table docs)
			"page int not null, " + //0-based index of the page
			"scaling int not null, " + //scaling factor, multiplied with factor 10000
			"image blob not null, " + //the tile image in PNG format
			"primary key (doc_id, page, scaling), " + "foreign key (doc_id) references docs(id))");
		//table "pageinfos": page formats
		sql(con, "create table pageinfos (" + "doc_id int not null, " + //ID of the document
			"page int not null, " + //0-based page index
			"width float not null, " + //width of the page in mm
			"height float not null, " + //height of the page in mm
			"primary key (doc_id, page), " + "foreign key (doc_id) references docs(id))");
		//table "scaledpageinfos": more information about scaled pages
		sql(con, "create table scaledpageinfos (" + "doc_id int not null, " + //ID of the document (table docs)
			"page int not null, " + //0-based page index
			"scaling int not null, " + //scaling factor, multiplied with factor 10000
			"widthpx int not null, " + //the width of the page in px
			"heightpx int not null, " + //the height of the page in px
			"primary key (doc_id, page, scaling), " + "foreign key (doc_id) references docs(id))");
		INSTANCE.log(Companion.remark("Database tables successfully created."));
	}

	private static void sql(Connection con, String sql)
		throws SQLException {
		con.prepareStatement(sql).executeUpdate();
	}

}
