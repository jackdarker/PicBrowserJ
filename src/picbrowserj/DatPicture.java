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
import java.util.Objects;

/**
 *stores information for a picture
 * @author jkhome
 */
public class DatPicture {
    
    /**
     *
     */
    public DatPicture(){
        ID = -1;
    }

    /**
     *
     * @param path
     * @param name
     */
    public DatPicture(String path, String name){
        ID = -1;
        Path = path;
        Name = name;        
    }

    /**
     *
     */
    public String Path;

    /**
     *
     */
    public String Name;

    /**
     *
     */
    public int ID;

    /**
     *
     */
    public int Rating;

    /**
     *
     */
    public int Status=0;

    /**
     *
     */
    public ArrayList<DatTag> Tags=new ArrayList<DatTag>();
    
    /**
     *
     * @param obj
     */
    public void removeTag(DatTag obj) {
        Tags.remove(obj);
    }

    /**
     *
     * @param obj
     */
    public void addTag(DatTag obj) {
        if (!Tags.contains(obj)) Tags.add(obj);
    }

    /**
     *
     * @param obj
     */
    public void setTags(ArrayList<DatTag> obj) {
        Tags= new ArrayList<DatTag>(obj);
    }

    /**
     *
     * @return
     */
    public Boolean RequiresTagUpload() {
        Boolean _ret=false;
        for(int i=0; i< Tags.size();i++) {
            _ret= !(Tags.get(i).IDListTags>=0 && Tags.get(i).IDTags>=0);
        }
        return _ret;
    }
    
    @Override
    public boolean equals(Object o) {
        DatPicture _Pic2 = (DatPicture)o;
        if(this.ID!= 0 && _Pic2.ID==this.ID) return true;
        
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.Path);
        hash = 97 * hash + this.ID;
        return hash;
    }
    @Override
    public String toString() {
        String _ret = ((ID>0)?"":"* ") +   Name;
        return _ret;
    }
    
}
