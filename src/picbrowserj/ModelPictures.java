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
    private static ArrayList<DatPicture> s_PicturesNew;
    
    private ModelPictures() {
        s_Tags = new ArrayList<DatTag>();
        s_Pictures = new ArrayList<DatPicture>();      
        s_PicturesNew = new ArrayList<DatPicture>();    
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
        // Todo set on FirstRun
        UpdateConfig("Store","C:/temp/Pics",0);
        int v= GetConfigInt("Version");
        //Todo DB init goes here  //////////////////////////////////////
        DatTag Tag;
        Tag= new DatTag();
        Tag.Text ="Animal";
        Tag.TagGroup="Animal";
        Tag.IsGroup=true;
        UpdateTags(Tag);
        Tag= new DatTag();
        Tag.Text ="Deer";
        Tag.TagGroup="Animal";
        Tag.IsGroup=false;
        UpdateTags(Tag);
        Tag= new DatTag();
        Tag.Text ="Fox";
        Tag.TagGroup="Animal";
        Tag.IsGroup=false;
        UpdateTags(Tag);
        DatPicture pic= new DatPicture();
        pic.Name ="90025-414.JPG";
        pic.Path="C:/Projects/Porsche_KBE/E1030342_20160713_PC_Porsche KBE 9X1 EOL/PROGRAM/OBJ_DATA/90025-414.JPG";
        pic.addTag(new DatTag("Deer","Animal"));
        UpdatePictures(pic);
        pic = new DatPicture();
        pic.Name ="90025-417.JPG";
        pic.Path="C:/Projects/Porsche_KBE/E1030342_20160713_PC_Porsche KBE 9X1 EOL/PROGRAM/OBJ_DATA/90025-417.JPG";
        pic.addTag(new DatTag("Fox","Animal"));
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
        String sql;
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
    private String GetConfigString(String Name) {
        Statement stmt = null;
        String sql;
        String Return="";
        Boolean Exists=false;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Name,ValueText,ValueInt FROM Config;" );
            while ( rs.next() ) {
                //Todo fix non exist
               Exists=true;
               Return = rs.getString("ValueString");
            }
            rs.close();
            stmt.close();
        }catch ( Exception e ) {
          HandleDBError( e);
       }
       return Return; 
    }
    private Boolean RefreshTagListID(DatTag Tag ) {
        Statement stmt = null;
        Boolean Update=false;
        ResultSet rs;
        try {
            stmt = s_DBConn.createStatement();
            rs = stmt.executeQuery( "SELECT ID,Tag,GroupX FROM ListTags where Tag='"+Tag.Text+"';" );
            while ( rs.next() ) {
               Update=true;
               Tag.IDListTags = rs.getInt("ID");
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
          HandleDBError( e);
        }    
        return Update;
    }
//updates/creates entry in Tag-Dictionary
    private void UpdateTags(DatTag Tag) {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;
        int id = -1;
        try {
          /*  stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Tag,GroupX FROM ListTags where Tag='"+Tag.Text+"';" );
            while ( rs.next() ) {
               Update=true;
               id = rs.getInt("ID");
            }
            rs.close();
            stmt.close();*/
          Update = RefreshTagListID(Tag);
            
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        if(Update) {
            sql = "Update ListTags Set Tag='"+Tag.Text+
                    "',GroupX='"+Tag.TagGroup+
                    "' where ID="+String.format("%d",Tag.IDListTags)+";";
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
        Update = RefreshTagListID(Tag);
    }
    private Boolean RefreshPictureID(DatPicture Pic ) {
        Statement stmt = null;
        Boolean Update=false;
        ResultSet rs;
        try {
            stmt = s_DBConn.createStatement();
            rs = stmt.executeQuery( "SELECT ID FROM Pictures where Path='"+Pic.Path+"';" );
            while ( rs.next() ) {
               Update=true;
               Pic.ID = rs.getInt("ID");
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
          HandleDBError( e);
        }    
        return Update;
    }
    //updates/creates Picture-Entry
    private void UpdatePictures(DatPicture Pic) {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;
        ResultSet rs;
        Update = RefreshPictureID(Pic);
        try {
    
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        if(Update) {
            sql = "Update Pictures Set Path='"+Pic.Path+
                    "',Name='"+Pic.Name+
                    "',Rating="+String.format("%d",Pic.Rating)+
                    " where ID="+String.format("%d",Pic.ID)+";";
        } else {
            sql = "INSERT INTO Pictures (Path,Name,Rating) " +
                     "VALUES ( '"+Pic.Path+"','"+Pic.Name +"',"+
                    String.format("%d",Pic.Rating)+");"; 
        
        }
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
        Update = RefreshPictureID(Pic);
        if (Pic.RequiresTagUpload()) UpdatePictureTags(Pic);
    }
    private Boolean RefreshTagID(DatTag Tag, DatPicture Pic ) {
        Statement stmt = null;
        Boolean Update=false;
        ResultSet rs;
        try {
            stmt = s_DBConn.createStatement();
            rs = stmt.executeQuery( "SELECT ID FROM Tags where IDListTags="+Tag.IDListTags+
                       " and IDPicture="+ Pic.ID +";" );
               while ( rs.next() ) {
                    //Update=true;
                    Tag.IDTags = rs.getInt("ID");
               }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
          HandleDBError( e);
        }    
        return Update;
    }
    //updates the tags of a picture, creates tags if necessary
    private void UpdatePictureTags(DatPicture Pic) {
        Statement stmt = null;
        String sql="";
        int TagID;
        int PicID;
        ResultSet rs;
        try {
            //PicID should already be set
            PicID = Pic.ID;
            assert(PicID>=0);
           //stmt = s_DBConn.createStatement();
           for(int i=0; i< Pic.Tags.size();i++) {
               UpdateTags(Pic.Tags.get(i));
               TagID = Pic.Tags.get(i).IDListTags; // should now have ID
               assert(TagID>=0);
               RefreshTagID(Pic.Tags.get(i),Pic);
              /* rs = stmt.executeQuery( "SELECT ID FROM Tags where IDListTags="+TagID+
                       " and IDPicture="+ PicID +";" );
               while ( rs.next() ) {
                    //Update=true;
                    Pic.Tags.get(i).IDTags = rs.getInt("ID");
               }
               rs.close();*/
           }
           //stmt.close();
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        for(int i=0; i< Pic.Tags.size();i++) {
            TagID = Pic.Tags.get(i).IDTags;
            if(TagID>=0) {
                sql = "Update Tags Set IDPicture="+String.format("%d",Pic.ID)+
                        ",IDListTags=" +String.format("%d",Pic.Tags.get(i).IDListTags)+
                        " where ID="+String.format("%d",TagID)+";";
            } else {
                sql = "INSERT INTO Tags (IDListTags,IDPicture) " +
                         "VALUES ("+
                        String.format("%d",Pic.Tags.get(i).IDListTags)+","+
                        String.format("%d",Pic.ID)+");"; 

            }
            stmt.executeUpdate(sql);
        }
        stmt.close();
        s_DBConn.commit();
        for(int i=0; i< Pic.Tags.size();i++) {
            RefreshTagID(Pic.Tags.get(i),Pic);
        }
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
        s_Pictures = new ArrayList<DatPicture>();
        Statement stmt = null;
        String sql="";
        DatPicture Pic;
        DatTag Tag;
        try {
            stmt = s_DBConn.createStatement();
            //loading pictures
            ResultSet rs = stmt.executeQuery( "SELECT ID,Path,Name,Rating From Pictures;" );
            while ( rs.next() ) {
                Pic= new DatPicture();
                Pic.ID= rs.getInt("ID");
                Pic.Name =rs.getString("Name");
                Pic.Path=rs.getString("Path");
                Pic.Rating = rs.getInt("Rating");
                s_Pictures.add(Pic);
            }
            rs.close();
            //loading tags for pictures
            for(int i=0; i< s_Pictures.size();i++) {
                rs = stmt.executeQuery( "SELECT Tags.ID,Tags.IDListTags,Tag,GroupX "+
                        "From Tags inner join ListTags on Tags.IDListTags=ListTags.ID where Tags.IDPicture="+
                        s_Pictures.get(i).ID +";" );
                while ( rs.next() ) {
                    Tag = new DatTag();
                    Tag.IDListTags = rs.getInt("IDListTags");
                    Tag.IDTags = rs.getInt("ID");
                    Tag.Text = rs.getString("Tag");
                    Tag.TagGroup = rs.getString("GroupX");
                    Tag.IsGroup = (Tag.Text==Tag.TagGroup);
                    s_Pictures.get(i).Tags.add(Tag);
                }
                rs.close();
            }
            stmt.close();
        
       } catch ( Exception e ) {
          HandleDBError( e);
       }
        
    }
    public void SavePicture(DatPicture Pic) {
        UpdatePictures(Pic);
    }
    public ArrayList<DatPicture> getAllPicture() {    
        return  s_Pictures;
    }
    public ArrayList<DatPicture> getNewPictures() {    
        return  s_PicturesNew;
    }
    /// Adds a newly selected Picture to the unsaved-Picture List
    /// If the Picture is already in the list, it is replaced
    /// If the Picture is already in DB it is ignored
    public void AddNewPicture(DatPicture Pic) {
        //Todo ??
        s_PicturesNew.add(Pic);
    }
    private void LoadTags() {
        s_Tags = new ArrayList<DatTag>();
        Statement stmt = null;
        String sql="";
        DatTag Tag;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Tag,GroupX FROM ListTags;" );
            while ( rs.next() ) {
                Tag= new DatTag();
                Tag.IDListTags= rs.getInt("ID");
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
