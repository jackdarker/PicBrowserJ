/*
 * Copyright (C) 2018 jkhome
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

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * stores information which Tag is child of which Tag
 * Map.add("Animal","Fish")
 * @author jkhome
 */

public class DatTagRel extends Vector<DatTagRel.Value> {
    public interface Predicate<T> {
    boolean test(T t);
    }
    public static <T> List<T> filterList(List<T> list, Predicate<T> predicate) {
        List<T> filtered = new Vector<>();
        for (T element:list){
            if(predicate.test(element)){
                filtered.add(element);
            }
        }
        return filtered;
    }

    class Value {
        public int Status=0;        //0 = new, 1=to delete , 2=
        public DatTag TagParent;
        public DatTag TagChild;
        public Value(DatTag tagParent, DatTag tagChild ) { 
            this.TagParent=tagParent;
            this.TagChild=tagChild;
        }
    }
    public DatTagRel() {
    }
    public List<DatTag> get(DatTag Tag, boolean IsChild) {
        List<DatTag> filtered = new Vector<>();
        for (DatTagRel.Value element:this){
            if (IsChild && element.TagChild.equals(Tag) && element.Status!=1 ) {
                filtered.add(element.TagParent);
            }
            if (!IsChild && element.TagParent.equals(Tag) && element.Status!=1 ) {
                filtered.add(element.TagChild);
            }
        }        
        return filtered;
    }
    public void addRelation( DatTag parent,DatTag sub){
        boolean _exists=false;
        for (DatTagRel.Value element:this){
            if (element.TagChild.equals(sub) && 
                element.TagParent.equals(parent)) {
                element.Status=0;
                _exists=true;
            }        
        }
        if(!_exists) this.add(new DatTagRel.Value(parent,sub));
    } 

    public void removeRelation( DatTag parent, DatTag sub){
        for (DatTagRel.Value element:this){
            if (element.TagChild.equals(sub) && 
                element.TagParent.equals(parent)) {
                element.Status=1;
            }
        }
    } 
    public void removeRelation( DatTag parent){
        for (DatTagRel.Value element:this){
            if (element.TagParent.equals(parent)) {
                element.Status=1;
            }
        }
    }

}
