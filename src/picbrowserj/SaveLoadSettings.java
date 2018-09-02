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

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author jkhome
 */
public class SaveLoadSettings {
    
    public static boolean init() {
        if (s_Instance==null) {
            s_Instance= new SaveLoadSettings();
            s_Instance.Load();
            return true;
        } else {
            return false;
        }
    }
    private org.ini4j.Ini Ini;
    private SaveLoadSettings() {
        Ini = new org.ini4j.Ini();    
    }
    private static SaveLoadSettings s_Instance;
    public static SaveLoadSettings getInstance() {
        if (s_Instance==null) {
            //Todo throw exception
        }
        return s_Instance;
    }
    private String s_File="user.properties";
    public void Load() {
        try {
            Ini.load(new FileInputStream(s_File));
        } catch (IOException e) {
            System.err.println("no user setup");
            Save();
        }

    }
    public void Save() {
        try {
            Ini.store(new FileOutputStream(s_File));
        } catch (IOException e) {
            //Todo ??
            System.err.println("no user setup");
        }
    }
    public Map<String,String> GetForms() {
        String Filter="Type";
        Map<String,String> _Windows = new HashMap<String,String>();
        Iterator<org.ini4j.Ini.Section> _all=Ini.values().iterator();
        while(_all.hasNext()) {
            org.ini4j.Ini.Section _sec=_all.next();
            if(_sec.containsKey(Filter)) {
                _Windows.put(_sec.getName() , _sec.get(Filter));
            }
        }
        return _Windows;
    }
    public void SetString(String Window, String Name , String Value){
        Ini.put(Window, Name, Value);
    }
    public String GetString(String Window, String Name) {
        String _Ret= Ini.get(Window,Name,String.class);
        return _Ret;
    }
    public void SetInt(String Window, String Name , Integer Value){
        String _rect= String.format("%d", Value);
        Ini.put(Window, Name, _rect);
    }
    public Integer GetInt(String Window, String Name) {
         Integer _Ret=0;
        String _rect= Ini.get(Window,Name,String.class);
        try {
            _Ret = Integer.decode(_rect);
        }
        catch(Exception Ex)
        {
            return null;
        }
        return _Ret;
    }
    public void SetRect(String Window, String Name , Rectangle Rect){
        String _rect= String.format("%d/%d/%d/%d", Rect.x,Rect.y,Rect.width,Rect.height);
        Ini.put(Window, Name, _rect);
    }
    public Rectangle GetRect(String Window, String Name) {
        String _rect= Ini.get(Window,Name,String.class);
        if(_rect==null) return null;
        String[] _Values =_rect.split("/");
        if(_Values.length!=4) return null;
        Rectangle rect= new Rectangle(Integer.decode(_Values[0]),Integer.decode(_Values[1]),
                Integer.decode(_Values[2]),Integer.decode(_Values[3]));
        //Rectangle _rect=Ini.get(Window,Name,Rectangle.class); cannot parse rectangle??
        return rect;
    }
}
