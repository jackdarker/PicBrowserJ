/*
 * Copyright (C) 2016 kubik_j1
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

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author kubik_j1
 */
public class ListCellRendererPicture extends DefaultListCellRenderer {

        Font font = new Font("arial", Font.PLAIN, 16);
        private Map<String, ImageIcon> m_Icons;
        
        public ListCellRendererPicture(Map<String, ImageIcon> IconMap) {
            super();
            m_Icons = IconMap;
        }
        
        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            label.setIcon(m_Icons.get(((DatPicture) value).Name));
            label.setVerticalTextPosition(BOTTOM);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setFont(font);
            return label;
        }
}
    
