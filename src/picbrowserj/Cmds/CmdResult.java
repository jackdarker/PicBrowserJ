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

import picbrowserj.Interface.Callable;
import picbrowserj.Interface.CmdResultInterface;

/**
 *
 * @author jkhome
 */
public class CmdResult implements CmdResultInterface{

    boolean m_OK;
    String m_Error;
    public CmdResult(boolean OK, String Error ) {
        m_Error=Error;
        m_OK=OK;
    }
    @Override
    public boolean OK() {
        return m_OK;
    }

    @Override
    public String NOKText() {
        return m_Error;
    }
}
