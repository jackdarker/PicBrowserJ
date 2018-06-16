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
package picbrowserj.Cmds;

import java.awt.Color;
import picbrowserj.Interface.CmdInterface;
import picbrowserj.ModelPictures;
import picbrowserj.DatTag;
import picbrowserj.Interface.Callable;
import picbrowserj.Interface.CmdResultInterface;

/**
 * find a Tag by its name
 * @author jkhome
 */
public class CmdFindTag extends CmdCreateTag {
    public CmdFindTag(String TagName, Callable<CmdResultAdd> PostAction) {
        super(TagName, PostAction);
    }
    @Override
    public void Undo() {  
    }

    @Override
    public boolean CanUndo() {
        return false;
    }

    @Override
    public void Redo() {
        try {
        if(m_NewTag==null) {
            ExecPostAction(new CmdResultAdd(false,"not a tag",null));
        } //??
            ExecPostAction(new CmdResultAdd(true,"", 
                ModelPictures.getInstance().getTagByText(m_NewTag.Text)));
        } catch(Exception ex) {
            java.util.logging.Logger.getLogger(CmdFindTag.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);;
        }
    }

    @Override
    public boolean CanRedo() {
        return false;
    }

    @Override
    public String GetText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
