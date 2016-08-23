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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author jkhome
 */
public class SaveLoadSettings {
    Properties applicationProps;
    
    public static boolean init() {
        if (s_Instance==null) {
            s_Instance= new SaveLoadSettings();
            s_Instance.Load();
            return true;
        } else {
            return false;
        }
    }
    private SaveLoadSettings() {}
    private static SaveLoadSettings s_Instance;
    public static SaveLoadSettings getInstance() {
        if (s_Instance==null) {
            //Todo throw exception
        }
        return s_Instance;
    }
    private String s_File="user.properties";
    public void Load() {
        // create and load default properties
        Properties defaultProps = new Properties();
        defaultProps.setProperty("MainBounds", "0;0;200;200");

        // create application properties with default
        applicationProps = new Properties(defaultProps);
        try {
            applicationProps.load(new FileInputStream(s_File));
        } catch (IOException e) {
            System.err.println("no user setup");
            Save();
        }

    }
    public void Save() {
        try {
            applicationProps.store(new FileOutputStream(s_File),"");
        } catch (IOException e) {
            //Todo ??
            System.err.println("no user setup");
        }
    }
    public Integer GetElementCount(String Element) {
        String _tmp=applicationProps.getProperty(Element);
        if (_tmp==null) return 0;
        return Integer.decode(_tmp);
    }
    public void SetElementCount(String Element, Integer Count) {
        applicationProps.setProperty(Element,
                String.format("%d",Count));
    }
    public void SetRect(String WindowName , Rectangle Rect){
        applicationProps.setProperty(WindowName,
                String.format("%d;%d;%d;%d;", 
                        Rect.x,Rect.y,Rect.width,Rect.height));
    }
    public Rectangle GetRect(String WindowName) {
        String _tmp=applicationProps.getProperty(WindowName);
        if (_tmp==null) return null;
        //Todo add checking
        String[] _tmpArr =_tmp.split(";");
        Rectangle _rect = new Rectangle(
                Integer.decode(_tmpArr[0]),Integer.decode(_tmpArr[1]),
                Integer.decode(_tmpArr[2]),Integer.decode(_tmpArr[3]));
        return _rect;
    }
}
