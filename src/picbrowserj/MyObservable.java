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

import java.util.Observable;

/**
 *
 * @author jkhome
 */
public class MyObservable extends Observable{
    
    public void NotifyPicChanged(UpdateReason Reason) {
            this.setChanged();
            this.notifyObservers(Reason);
    }
    public enum updateReasonEnum {
        Pics_added, //Picture loaded for browsing
        Pics_moved, 
        Pics_viewed, //Picture selected for viewing
        Pics_new; 
    }
    public class UpdateReason {
        public updateReasonEnum Reason;
        public DatPicture Picture=null;
        public String Source ="";
        public UpdateReason(updateReasonEnum reason, DatPicture Pic) {
            Picture = Pic;
            Reason = reason;
            
        }
        public UpdateReason(updateReasonEnum reason, String File) {
            Source = File;
            Reason = reason;
            
        }

    }
}
