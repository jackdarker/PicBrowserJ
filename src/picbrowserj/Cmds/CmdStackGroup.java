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

/// <summary>

import java.util.Vector;
import picbrowserj.Interface.CmdInterface;
import picbrowserj.Interface.CmdStackListener;

/// Manages several CmdStack. 
/// Mainly used for MDI application where you want to use one Stack for each document.
/// </summary>
public class CmdStackGroup extends CmdStack implements CmdStackListener
{
    public CmdStackGroup()
    {
        m_Stacks = new Vector<>();
        m_ActiveStack = null;
    }
    public CmdStack GetActiveStack()
    {
        return m_ActiveStack;
    }
    @Override
    public String GetUndoText()
    {
        if (!CanUndo()) return "";
        return m_ActiveStack.GetUndoText();
    }
    @Override
    public String GetRedoText()
    {
        if (!CanRedo()) return "";
        return m_ActiveStack.GetRedoText();
    }
    public void SetActiveStack(CmdStack Stack)
    {
        if (Stack==null||m_Stacks.contains(Stack))
        {
            m_ActiveStack = Stack;
            super.onEventCanRedoChanged();
            super.onEventCanUndoChanged();
            super.onEventUpdate();
        }
    }
    public void AddStack(CmdStack Stack)
    {
        if (!m_Stacks.contains(Stack))
        {
            m_Stacks.add(Stack);
            Stack.addListener(this);
        }
    }
    @Override
    public void EventCanRedoChanged() {
        super.onEventCanRedoChanged();
    }

    @Override
    public void EventCanUndoChanged() {
        super.onEventCanUndoChanged();
    }

    @Override
    public void EventUpdate() {
       super.onEventUpdate();
    }
    
    @Override
    public void Push(CmdInterface Command)
    {
        if (m_ActiveStack == null) return ;
        m_ActiveStack.Push(Command);
    }
    public void RemoveStack(CmdStack Stack)
    {
        if (m_Stacks.contains(Stack))
        {
            Stack.removeListener(this);
            m_Stacks.remove(Stack);
        };
        if (m_ActiveStack == Stack) {
            SetActiveStack(null);
        }
    }
    @Override
    public Boolean CanRedo()
    {
        if (m_ActiveStack == null) return false;
        return m_ActiveStack.CanRedo();
    }
    @Override
    public Boolean CanUndo()
    {
        if (m_ActiveStack == null) return false;
        return m_ActiveStack.CanUndo();
    }
    @Override
    public void Undo()
    {
        if (CanUndo())
        {
            m_ActiveStack.Undo();
            super.onEventCanRedoChanged();
            super.onEventCanUndoChanged();
            super.onEventUpdate();
        }
    }
    @Override
    public void Redo()
    {
        if (CanRedo()) m_ActiveStack.Redo();
        super.onEventCanRedoChanged();
        super.onEventCanUndoChanged();
        super.onEventUpdate();
    }

    CmdStack m_ActiveStack;
    Vector<CmdStack> m_Stacks;

    
}