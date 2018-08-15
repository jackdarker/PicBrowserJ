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
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.sql.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author jkhome
 */
interface ModelListener {
    void EventPics_added(DatPicture Picture); //Picture loaded for browsing
    void EventPics_moved(DatPicture Picture); 
    void EventPics_viewed(DatPicture Picture); //Picture selected for viewing
    void EventPics_new(DatPicture Picture); 
}
public class ModelPictures extends picbrowserj.Observable<ModelListener>{
    private static ModelPictures s_Instance;
    private static Connection s_DBConn;
    private static ArrayList<DatTag> s_Tags;
    private static DatTagRel s_TagsRelation;
    private static ArrayList<DatPicture> s_Pictures;
    private static ArrayList<DatPicture> s_PicturesNew;
    
    private ModelPictures() {
        s_Tags = new ArrayList<DatTag>();
        s_TagsRelation = new DatTagRel();
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
          sql = "CREATE TABLE Pictures " +      //definition of all pictures
                       "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                       " Path           TEXT    NOT NULL, " + 
                       " Name        CHAR(50), " + 
                       " Status        INT, " + 
                       " Rating         REAL)"; 
          stmt.executeUpdate(sql);
          stmt.close();
          stmt = s_DBConn.createStatement();
          sql = "CREATE TABLE TagDef " +        //definition of a Tag
                  "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                  " Color        INT, " + 
                  " Status        INT, " + 
                  " Tag        CHAR(50)) " ; 
          stmt.executeUpdate(sql);
          stmt.close();
          stmt = s_DBConn.createStatement();
          sql = "CREATE TABLE TagRel " +        //definition how one Tag relates to another
                       "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                       " Status        INT, " + 
                       " ParentID        INT, " + 
                       " SubID        INT ) "  ; 
          stmt.executeUpdate(sql);
          stmt.close();
          stmt = s_DBConn.createStatement();
          sql = "CREATE TABLE Tags " +          //definition what tags a picture has
                       "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
                       " Status        INT, " + 
                       " IDListTags            INT     NOT NULL, " + 
                       "IDPicture            INT     NOT NULL )" ; 
          stmt.executeUpdate(sql);
          stmt.close();
          }
        
        UpdateConfig("Version","",100);
        createTestData();
        //////////////////////////////////////////////////////////////////
       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    private void createTestData(){
        // Todo set on FirstRun
        UpdateConfig("Store","d:/temp",0);
        int v= GetConfigInt("Version");
        //Todo DB init goes here  //////////////////////////////////////
        DatTag Tag;
        Tag= new DatTag();
        Tag.Text ="Animal";
        Tag.BGColor=Color.cyan;
        UpdateTags(Tag);
        Tag= new DatTag();
        Tag.Text ="Deer";
        Tag.BGColor=Color.LIGHT_GRAY;
        UpdateTags(Tag);
        Tag= new DatTag();
        Tag.Text ="Fox";
        Tag.BGColor=Color.LIGHT_GRAY;
        UpdateTags(Tag);
        DatPicture pic= new DatPicture();
        pic.Name ="20150628_115213.jpg";
        pic.Path="D:/temp/artists/artists3/20150628_115213.jpg";
        pic.addTag(new DatTag("Deer",Color.GREEN));
        UpdatePictures(pic);
        pic = new DatPicture();
        pic.Name ="TPhoto_00001.jpg";
        pic.Path="D:/temp/artists/artists3/TPhoto_00001.jpg";
        pic.addTag(new DatTag("Fox",Color.GREEN));
        UpdatePictures(pic);
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
            rs = stmt.executeQuery( "SELECT ID,Color,Tag FROM TagDef where Tag='"+Tag.Text+"';" );
            while ( rs.next() ) {
               Update=true;
               Tag.IDListTags = rs.getInt("ID");
               Tag.BGColor= new Color(rs.getInt("Color"));
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
          Update = RefreshTagListID(Tag);
            
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        if(Update) {
            sql = "Update TagDef Set Tag='"+Tag.Text+"',Color="+ 
                    String.format("%d",Tag.BGColor.getRGB()) +
                   ",Status="+String.format("%d",Tag.Status)+
                    " where ID="+String.format("%d",Tag.IDListTags)+";";
        } else {
            sql = "INSERT INTO TagDef (Tag,Color,Status) " +
                     "VALUES ( '"+Tag.Text+
                    "',"+String.format("%d",Tag.BGColor.getRGB())+
                    ", "+String.format("%d",Tag.Status)+
                    ");"; 
        }
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();
        CleanupDeadLinks();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
        if(!Update) {   //tag is new, add them to local collection
           RefreshTagListID(Tag); //fetch ID after Insert
           s_Tags.add(Tag);
        }
        
    }
    //Todo delete entrys with IDs pointing nowhere
    private void CleanupDeadLinks() {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;
        int id = -1;
        try {            
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        sql = "Delete from TagDef where Status=1;";     //TODO also remove Relations to pictures and childtags: 
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
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
    //updates/creates entry in Tag-Dictionary
    private void UpdateTagRelation( ) {
        Statement stmt = null;
        String sql="";
        Boolean Update=false;
        int id = -1;
        try {
        s_DBConn.setAutoCommit(false);
        stmt = s_DBConn.createStatement();
        for(DatTagRel.Value e:s_TagsRelation) {

    /*UPDATE tableName SET VALUE='newValue'  WHERE COLUMN='identifier'
    IF @@ROWCOUNT=0
        INSERT INTO tableName VALUES (...)*/
            sql = "INSERT OR REPLACE INTO TagRel (ParentID,SubID,Status) " +
                     "VALUES ("+ String.format("%d",e.TagParent.IDListTags)+
                ", "+String.format("%d",e.TagChild.IDListTags)+
                ", "+String.format("%d",e.Status)+
                ");"; 
        
        stmt.executeUpdate(sql);
        }
        sql = "Delete from TagRel where Status=1;";
        stmt.executeUpdate(sql);
        stmt.close();
        s_DBConn.commit();

       } catch ( Exception e ) {
          HandleDBError( e);
       }
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
                rs = stmt.executeQuery( "SELECT Tags.ID,Tags.IDListTags,Tag "+
                        "From Tags inner join TagDef on Tags.IDListTags=TagDef.ID where Tags.IDPicture="+
                        s_Pictures.get(i).ID +";" );
                while ( rs.next() ) {
                    Tag = new DatTag();
                    Tag.IDListTags = rs.getInt("IDListTags");
                    Tag.IDTags = rs.getInt("ID");
                    Tag.Text = rs.getString("Tag");
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
    //Moves a picture physically to a directory
    //if the picture exists in DB - updates the data in DB
    //fires eventPics_moved when done
    public void MovePicture(DatPicture Pic, Path NewPath) throws IOException {
        //we cannot assume that Pic is already in DB ! Only path might be set.
        Path _old = Paths.get(Pic.Path);
        //Path _new = Paths.get(NewPath+"/"+_old.getFileName());
        
        //TODO show dialog to ack overwrite/renaming of files
        Files.move(_old, NewPath, REPLACE_EXISTING);
        Pic.Path=NewPath.toString();
        //TODO save picture in DB or update path
        this.onEventPics_moved(Pic);
    }
    /// Adds a newly selected Picture to the unsaved-Picture List
    /// If the Picture is already in the list, it is replaced
    /// If the Picture is already in DB it is ignored
    public void AddNewPicture(DatPicture Pic) {
        //Todo ??
        s_PicturesNew.add(Pic);
    }
    public DatPicture getPictureByPath(String Text) {
        DatPicture x;
        Iterator<DatPicture> it= s_Pictures.iterator();
        while (it.hasNext()) {
            x=it.next();
            if (x.Path.compareTo(Text)==0)
                return x;
        }
        return null;
    }
    public DatPicture getPictureByID(int ID) {
        DatPicture x;
        Iterator<DatPicture> it= s_Pictures.iterator();
        while (it.hasNext()) {
            x=it.next();
            if (x.ID==ID)
                return x;
        }
        return null;
    }
    private void LoadTags() {
        s_Tags.clear();//s_Tags = new ArrayList<DatTag>();
        //??HashMap<Integer,DatTag> IDList = new HashMap<Integer,DatTag>();
        s_TagsRelation.clear(); // = new DatTagRel();
        Statement stmt = null;
        String sql="";
        DatTag Tag;
        DatTag Tag2;
        try {
            stmt = s_DBConn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID,Tag,Color FROM TagDef;" );
            while ( rs.next() ) {
                Tag= new DatTag();
                Tag.IDListTags= rs.getInt("ID");
                Tag.BGColor= new Color(rs.getInt("Color"));
                Tag.Text =rs.getString("Tag");
                s_Tags.add(Tag);
                //IDList.put(Tag.IDListTags, Tag);
            }
            rs.close();
            stmt.close();
            stmt = s_DBConn.createStatement();
            rs = stmt.executeQuery( "SELECT ID,ParentID,SubID FROM TagRel where Status!=1;" );
            while ( rs.next() ) {
                int ParentID = rs.getInt("ParentID");
                int SubID =rs.getInt("SubID");
                Tag = getTagByID(ParentID);
                Tag2 = getTagByID(SubID);
                s_TagsRelation.addRelation(Tag, Tag2);
            }
            rs.close();
            stmt.close();
       } catch ( Exception e ) {
          HandleDBError( e);
       }
    }
    public DatTag getTagByText(String Text) {
        DatTag x;
        Iterator<DatTag> it= s_Tags.iterator();
        while (it.hasNext()) {
            x=it.next();
            if (x.Text.compareTo(Text)==0)
                return x;
        }
        return null;
    }
    public DatTag getTagByID(int ID) {
        DatTag x;
        Iterator<DatTag> it= s_Tags.iterator();
        while (it.hasNext()) {
            x=it.next();
            if (x.IDListTags==ID)
                return x;
        }
        return null;
    }
    
    public DatTag addTag(DatTag Tag) {
        UpdateTags(Tag);
        return Tag;
    }
    //TODO
    public DatTag deleteTag(DatTag Tag) {
        removeRelation(Tag);
        
        return Tag;
    }
    
    public void addRelation( DatTag parent,DatTag sub){
        s_TagsRelation.addRelation(parent, sub);
        UpdateTagRelation();
    }
    public void removeRelation( DatTag parent, DatTag sub){
        s_TagsRelation.removeRelation(parent, sub);
        UpdateTagRelation();
    }
    public void removeRelation( DatTag parent){
        s_TagsRelation.removeRelation(parent);
        UpdateTagRelation();
    }
    public List<DatTag> getParentTags(DatTag Me) {
        List<DatTag> rel=s_TagsRelation.get(Me,true);
        return rel;
    }
    public List<DatTag> getSubTags(DatTag Me) {
        List<DatTag> rel=s_TagsRelation.get(Me,false);
        return rel;
    }
    //returns an unmodifiableList List of all Tags
    public List<DatTag> getAllTags() {
        return Collections.unmodifiableList(Collections.list(Collections.enumeration(s_Tags)));
    }
    void onEventPics_added(DatPicture Picture) {
        SrvPicManager.getInstance().EventPics_added(Picture);//for(ModelListener l: listeners) l.EventPics_added(Picture);
    }
    void onEventPics_moved(DatPicture Picture){
        SrvPicManager.getInstance().EventPics_moved(Picture);//for(ModelListener l: listeners) l.EventPics_moved(Picture);
    } 
    void onEventPics_viewed(DatPicture Picture){
        SrvPicManager.getInstance().EventPics_viewed(Picture);//for(ModelListener l: listeners) l.EventPics_viewed(Picture);
    }
    void onEventPics_new(DatPicture Picture){
        SrvPicManager.getInstance().EventPics_new(Picture);//for(ModelListener l: listeners) l.EventPics_new(Picture);
    }
}
