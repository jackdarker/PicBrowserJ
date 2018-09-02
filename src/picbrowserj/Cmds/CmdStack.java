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
import picbrowserj.Interface.CmdInterface;
import picbrowserj.Interface.CmdStackListener;
import java.util.Vector;


/**
 *
 * @author jkhome
 * Undo-Command-Stack
*  Stores and Executes Commands for Undo and Redo
 */
public class CmdStack extends picbrowserj.Observable<CmdStackListener>     {

    /**
     *
     */
    protected void onEventCanRedoChanged() {
         for(CmdStackListener l: listeners) l.EventCanRedoChanged();
    }

    /**
     *
     */
    protected void onEventCanUndoChanged() {
         for(CmdStackListener l: listeners) l.EventCanUndoChanged();
    }

    /**
     *
     */
    protected void onEventUpdate() {
         for(CmdStackListener l: listeners) l.EventUpdate();
    }
    private int m_MaxUndo=10 ;
    private int m_CurrentCmd;
    private final Vector<picbrowserj.Interface.CmdInterface> m_CmdStack;

    /**
     *
     */
    public CmdStack()
    {
        m_CmdStack = new Vector<picbrowserj.Interface.CmdInterface>();
        m_CurrentCmd = -1;
    }
    /// <summary>
    /// Connect this to an Event-Source (f.e. button click) to trigger Redo
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>

    /**
     *
     * @param e
     */
    public void RedoEvent(java.awt.event.ActionEvent e)
    {
        if (this != null) Redo();
    }
    /// <summary>
    /// Connect this to an Event-Source (f.e. button click) to trigger Undo
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>

    /**
     *
     * @param e
     */
    public void UndoEvent(java.awt.event.ActionEvent e)
    {
        if (this != null) Undo();
    }        
    
    /// <summary>
    /// Pushs a command on the undo-stack and executes redo
    /// </summary>
    /// <param name="Command"></param>

    /**
     *
     * @param Command
     */
    public void Push(CmdInterface Command)
    {
        if (CanRedo())
        {
            m_CmdStack.removeAll(m_CmdStack.subList(
                    (m_CurrentCmd+1), (m_CmdStack.size() - m_CurrentCmd-1)));
        };
        if (!Command.IgnoreAsUndoRedo()) {
            m_CmdStack.add(Command);
            if (m_CmdStack.size() > m_MaxUndo)
            {
                m_CmdStack.retainAll(m_CmdStack.subList(0,m_CmdStack.size() - m_MaxUndo));
            }
            m_CurrentCmd = Math.max(-1, m_CmdStack.size() - 2);
            Redo();
        } else {
            Command.Redo(); //just execute and forget
        }
        
    }

    /**
     * returns if the stack has an operation that can be Un-undone.
     * @return
     */
    public Boolean CanRedo()
    {
        return m_CurrentCmd < m_CmdStack.size()-1 ;
    }

    /**
     *
     * @return
     */
    public Boolean CanUndo()
    {
        return m_CurrentCmd >= 0;
    }
    /// <summary>
    /// Redos the last command on the stack if there is one
    /// </summary>

    /**
     *
     */
    public void Redo()
    {
        if (CanRedo())
        {
            m_CurrentCmd++;
            m_CmdStack.elementAt(m_CurrentCmd).Redo();
            this.onEventCanRedoChanged();
            this.onEventCanUndoChanged();
            this.onEventUpdate();
        }
    }

    /**
     *Undos the last command on the stack if there is one
     */
    public void Undo()
    {
        if (CanUndo())
        { 
            m_CmdStack.elementAt(m_CurrentCmd).Undo(); 
            m_CurrentCmd--;
            this.onEventCanRedoChanged();
            this.onEventCanUndoChanged();
            this.onEventUpdate();
        }
    }

    /**
     *Get textinfo of the Undo of the current command on the stack
     * @return
     */
    public String GetUndoText()
    {
        if(!CanUndo()) return "Undo";
        return "Undo " + m_CmdStack.elementAt(m_CurrentCmd).GetText();
    }
    /// <summary>
    /// Get textinfo of the Redo of the current command on the stack
    /// </summary>
    /// <returns></returns>

    /**
     *
     * @return
     */
    public String GetRedoText()
    {
        if (!CanRedo()) return "Redo";
        return "Redo " + m_CmdStack.elementAt(m_CurrentCmd+1).GetText();
    }

}
