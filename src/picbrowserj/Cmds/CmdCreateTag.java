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
 *
 * @author jkhome
 */
public class CmdCreateTag implements CmdInterface {

    public class CmdResultAdd extends CmdResult {
        DatTag m_DatTagResult;
        public CmdResultAdd(boolean OK, String Error, DatTag Result)  {
            super(OK,Error);
            m_DatTagResult=Result;
        }
        public DatTag Result() {
            return m_DatTagResult;
        }
    }
    CmdInterface.UndoState m_Done=UndoState.BeforeFirstRun;
    protected DatTag m_NewTag=null;
    protected Callable<CmdResultAdd> m_PostAction;
    public CmdCreateTag(String TagName, Callable<CmdResultAdd> PostAction) {
        if (!TagName.isEmpty())
            m_NewTag = new DatTag(TagName,Color.CYAN);
        m_PostAction = PostAction;
    }
    protected void ExecPostAction(CmdResultAdd Result)  throws Exception {
        if (m_PostAction!=null) m_PostAction.call(Result);
    }
    @Override
    public void Undo() {  
        try {
        if(m_NewTag==null) {
            ExecPostAction(new CmdResultAdd(false,"not a tag",null));
        } else {
            ExecPostAction(new CmdResultAdd(true,"", 
                ModelPictures.getInstance().deleteTag(m_NewTag)));
            m_Done= UndoState.Undone;
        }
        } catch(Exception ex) {
            java.util.logging.Logger.getLogger(CmdCreateTag.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean IgnoreAsUndoRedo() {
        return false;
    }
    @Override
    public boolean CanUndo() {
        return m_Done==UndoState.Done;
    }

    @Override
    public void Redo() {
        try {
        if(m_NewTag==null) {
            ExecPostAction(new CmdResultAdd(false,"not a tag",null));
        } else {
            ExecPostAction(new CmdResultAdd(true,"", 
                ModelPictures.getInstance().addTag(m_NewTag)));
            m_Done=UndoState.Done;
        }
        } catch(Exception ex) {
            java.util.logging.Logger.getLogger(CmdAssignTags.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean CanRedo() {
        return m_Done==UndoState.Undone;
    }

    @Override
    public String GetText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
