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
import java.util.HashSet;
/**
 *This represents a tag/label
 * f.e. Tag "Deer" from TagGroup "Animal"
 * @author jkhome
 */
public class DatTag  {
    public DatTag(){
        IDListTags=-1;
        IDTags=-1;
        Text="new Tag";
        BGColor= java.awt.Color.RED;
    }
    public DatTag(String Tag,Color color){
        IDListTags=-1;
        IDTags=-1;
        Text = Tag;
        BGColor= color;
    }
    public String Text;
    public Color BGColor;
    public Boolean IsGroup;
    public int IDListTags;  //primarykey of the tag-definition
    public int IDTags;  //primarykey of the tag-picture relation
    

}
