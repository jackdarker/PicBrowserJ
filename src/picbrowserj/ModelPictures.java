/*
 * Copyright (C) 2016 jkhome
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package picbrowserj;

import java.util.ArrayList;
import java.sql.*;
/**
 *
 * @author jkhome
 */
public class ModelPictures {
    private static ModelPictures s_Instance;
    private static Connection s_DBConn;
    private static ArrayList<DatTag> s_Tags;
    private static ArrayList<DatPicture> s_Pictures;
    
    private ModelPictures() {
        s_Tags = new ArrayList<>();
        s_Pictures = new ArrayList<>();        
        InitDB();
        LoadTags();
        LoadPictures();
    }
    public static boolean init() {
        if (s_Instance==null) {
            s_Instance= new ModelPictures();
            return true;
        } else {
            return false;
        }
    }
    public static boolean shutdown () {
        return true;
    }
    public static ModelPictures getInstance() {
        if (s_Instance==null) {
            //Todo throw exception
        }
        return s_Instance;
    }
    private void InitDB() {
        Statement stmt = null;
        Boolean Init =true;
        try {
            Class.forName("org.sqlite.JDBC");
            s_DBConn = DriverManager.getConnection("jdbc:sqlite:PicBowser.db");

            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='Config' COLLATE NOCASE" );
            while ( rs.next() ) {
                Init=(rs.getInt(1)==0);
            }
            rs.close();    
            stmt.close();

          if (Init) {
          stmt = s_DBConn.createStatement();
          String sql = "CREATE TABLE Config " +
                       "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                       " Name           TEXT    NOT NULL, " + 
                       " ValueInt            INT , " + 
                       " ValueText       TEXT )"; 
          stmt.executeUpdate(sql);
          stmt.close();
          stmt = s_DBConn.createStatement();
          sql = "CREATE TABLE Pictures " +
                       "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                       " Path           TEXT    NOT NULL, " + 
                       " IDTags            INT     NOT NULL, " + 
                       " Name        CHAR(50), " + 
                       " Rating         REAL)"; 
          stmt.executeUpdate(sql);
          stmt.close();
          stmt = s_DBConn.createStatement();
          sql = "CREATE TABLE ListTags " +
                       "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                       " Tag        CHAR(50), " + 
                       " GroupX        CHAR(50) ) " ; 
          stmt.executeUpdate(sql);
          stmt.close();
          stmt = s_DBConn.createStatement();
          sql = "CREATE TABLE Tags " +
                       "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                       " IDListTags            INT     NOT NULL, " + 
                       "IDPicture            INT     NOT NULL )" ; 
          stmt.executeUpdate(sql);
          stmt.close();
          }
        
        UpdateConfig("Version","",100);
        int v= GetConfigInt("Version");
        //Todo DB init goes here  //////////////////////////////////////
        DatTag Tag= new DatTag();
        Tag.Text ="Animal";
        Tag.TagGroup="Animal";
        Tag.IsGroup=true;
        UpdateTags(Tag);
        Tag.Text ="Deer";
        Tag.TagGroup="Animal";
        Tag.IsGroup=false;
        UpdateTags(Tag);
        DatPicture pic= new DatPicture();
        pic.Name ="";
        pic.Path="D:/furries/andere/1c.jpg";
        UpdatePictures(pic);
        pic.Name ="";
        pic.Path="D:/furries/andere/01.jpg";
        UpdatePictures(pic);
        //////////////////////////////////////////////////////////////////
       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    private void HandleDBError( Exception e) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
    }
    private int GetConfigInt(String Name) {
        Statement stmt = null;
        String sql="";
        int Return=0;
        Boolean Exists=false;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Name,ValueText,ValueInt FROM Config;" );
            while ( rs.next() ) {
                //Todo fix non exist
               Exists=true;
               Return = rs.getInt("ValueInt");
            }
            rs.close();
            stmt.close();
        }catch ( Exception e ) {
          HandleDBError( e);
       }
       return Return; 
    }
    private void UpdateTags(DatTag Tag) {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;
        int id = -1;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Tag,GroupX FROM ListTags where Tag='"+Tag.Text+"';" );
            while ( rs.next() ) {
               Update=true;
               id = rs.getInt("ID");
            }
            rs.close();
            stmt.close();
            
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        if(Update) {
            sql = "Update ListTags Set Tag='"+Tag.Text+
                    "',GroupX='"+Tag.TagGroup+
                    "' where ID="+String.format("%d",id)+";";
        } else {
            sql = "INSERT INTO ListTags (Tag,GroupX) " +
                     "VALUES ( '"+Tag.Text+"','"+Tag.TagGroup +"');"; 
        }
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    private void UpdatePictures(DatPicture Pic) {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;

        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID FROM Pictures where Path='"+Pic.Path+"';" );
            while ( rs.next() ) {
               Update=true;
               Pic.ID = rs.getInt("ID");
            }
            rs.close();
            stmt.close();
    
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        if(Update) {
            sql = "Update Pictures Set Path='"+Pic.Path+
                    "',Name='"+Pic.Name+
                    "',IDTags="+String.format("%d",Pic.IDTagList)+
                    "',Rating="+String.format("%f",Pic.Rating)+
                    "' where ID="+String.format("%d",Pic.ID)+";";
        } else {
            sql = "INSERT INTO Pictures (Path,Name,IDTags,Rating) " +
                     "VALUES ( '"+Pic.Path+"','"+Pic.Name +"',"+
                    String.format("%d",Pic.IDTagList)+","+
                    String.format("%d",Pic.Rating)+");"; 
        
        }
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    private void UpdatePictureTags(DatPicture Pic) {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID FROM Tags where IDListTags="+Pic.IDTagList+";" );
            while ( rs.next() ) {
               Update=true;
            Arraylist durchlaufen   Pic.IDTagList = rs.getInt("ID");
            }
            rs.close();
            stmt.close();

        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        if(Update) {
            sql = "Update Tags Set IDPicture="+String.format("%d",Pic.ID)+
                    " where ID="+String.format("%d",Pic.IDTagList)+";";
        } else {
            sql = "INSERT INTO Tags (IDListTags,IDPicture) " +
                     "VALUES ("+
                    String.format("%d",Pic.IDTagList)+","+
                    String.format("%d",Pic.ID)+");"; 
        
        }
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    private void UpdateConfig(String Name, String ValueString, int ValueInt) {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Name,ValueText,ValueInt FROM Config;" );
            while ( rs.next() ) {
               Update=true;
               int id = rs.getInt("ID");
               String  name = rs.getString("Name");
            }
            rs.close();
            stmt.close();
            
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        if(Update) {
            sql = "Update Config Set ValueText='"+ValueString+
                    "',ValueInt="+String.format("%d",ValueInt)+
                    " where Name='"+Name+"';";
        } else {
            sql = "INSERT INTO Config (Name,ValueText,ValueInt) " +
                     "VALUES ( '"+Name+"','"+ValueString +"',"+
                        String.format("%d",ValueInt)+");"; 
        }
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    private void LoadPictures() {    
        s_Pictures = new ArrayList<>();
        Statement stmt = null;
        String sql="";
        DatPicture Pic;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Path,Name,IDTags,Rating From Pictures;" );
            while ( rs.next() ) {
                Pic= new DatPicture();
                Pic.ID= rs.getInt("ID");
                Pic.Name =rs.getString("Name");
                Pic.Path=rs.getString("Path");
                Pic.IDTagList = rs.getInt("IDTags");
                Pic.Rating = rs.getDouble("Rating");
                s_Pictures.add(Pic);
            }
            rs.close();
            stmt.close();
        
       } catch ( Exception e ) {
          HandleDBError( e);
       }
        
    }
    public ArrayList<DatPicture> getAllPicture() {    
        return  s_Pictures;
    }
    private void LoadTags() {
        s_Tags = new ArrayList<>();
        Statement stmt = null;
        String sql="";
        DatTag Tag;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Tag,GroupX FROM ListTags;" );
            while ( rs.next() ) {
                Tag= new DatTag();
                Tag.ID= rs.getInt("ID");
                Tag.Text =rs.getString("Tag");
                Tag.TagGroup=rs.getString("GroupX");
                Tag.IsGroup= (Tag.Text==Tag.TagGroup);
                s_Tags.add(Tag);
            }
            rs.close();
            stmt.close();
        
       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    public ArrayList<DatTag> getAllTags() {
        return  s_Tags;
    }
}
