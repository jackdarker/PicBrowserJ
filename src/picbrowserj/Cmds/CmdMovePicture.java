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

import java.nio.file.Path;
import picbrowserj.DatPicture;
import picbrowserj.Interface.CmdInterface;
import picbrowserj.Interface.Callable;
import picbrowserj.ModelPictures;
import picbrowserj.SrvPicManager;

/**
 *
 * @author jkhome
 */
public class CmdMovePicture implements CmdInterface {

    protected DatPicture m_Picture=null;
    protected Path m_NewPath;
    protected Callable<CmdResult> m_PostAction;
    public CmdMovePicture(DatPicture Picture, Path NewPath,Callable<CmdResult> PostAction) {
        m_Picture = Picture;
        m_NewPath=NewPath;
        m_PostAction = PostAction;
    }
    protected void ExecPostAction(CmdResult Result)  throws Exception {
        if (m_PostAction!=null) m_PostAction.call(Result);
    }
    @Override
    public void Undo() {  
        try {
            ExecPostAction(new CmdResult(false, "not supported"));
        } catch(Exception ex) {
            java.util.logging.Logger.getLogger(CmdMovePicture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);;
        }
    }

    @Override
    public boolean CanUndo() {
        return false;
    }
    @Override
    public boolean IgnoreAsUndoRedo() {
        return true;
    }
    @Override
    public void Redo() {
        try {
        if(m_Picture==null) {
            ExecPostAction(new CmdResult(false,"not a picture"));
        } else {
            ModelPictures.getInstance().MovePicture(m_Picture,m_NewPath);
            ExecPostAction(new CmdResult(true,""));
        }
        } catch(Exception ex) {
             java.util.logging.Logger.getLogger(CmdMovePicture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
