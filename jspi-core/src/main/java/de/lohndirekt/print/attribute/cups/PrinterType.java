/**
 * Copyright (C) 2003 <a href="http://www.lohndirekt.de/">lohndirekt.de</a>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 */
package de.lohndirekt.print.attribute.cups;

import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintServiceAttribute;

public class PrinterType extends IntegerSyntax implements PrintServiceAttribute {

    public static final int IS_PRINTER_CLASS = 1 << 0;
    public static final int IS_REMOTE_DESTINATION = 1 << 1;
    public static final int CAN_BLACK = 1 << 2;
    public static final int CAN_COLOR = 1 << 3;
    public static final int CAN_DUPLEX = 1 << 4;
    public static final int CAN_STAPLE = 1 << 5;
    public static final int CAN_UNCOLLATED = 1 << 6;
    public static final int CAN_COLLATED = 1 << 7;
    public static final int CAN_PUNCH = 1 << 8;
    public static final int CAN_COVER = 1 << 9;
    public static final int CAN_BIND = 1 << 10;
    public static final int CAN_SORT = 1 << 11;
    public static final int CAN_A4 = 1 << 12;
    public static final int CAN_A2 = 1 << 13;
    public static final int CAN_LARGER_A2 = 1 << 14;
    public static final int CAN_USER_DEFINED_MEDIA_SIZE = 1 << 15;
    public static final int IS_IMPLICIT_CLASS = 1 << 16;

    public boolean isPrinterClass() {
        return (getValue() & IS_PRINTER_CLASS) == 1;
    }

    public boolean isRemoteDestination() {
        return (getValue() & IS_REMOTE_DESTINATION) == 1;
    }

    public boolean canPrintBlack() {
        return (getValue() & CAN_BLACK) == 1;
    }

    public boolean canPrintColor() {
        return (getValue() & CAN_COLOR) == 1;
    }

    public boolean canDuplex() {
        return (getValue() & CAN_DUPLEX) == 1;
    }

    public boolean canStaple() {
        return (getValue() & CAN_STAPLE) == 1;
    }

    public boolean canUncollated() {
        return (getValue() & CAN_UNCOLLATED) == 1;
    }

    public boolean canCollated() {
        return (getValue() & CAN_COLLATED) == 1;
    }

    public boolean canPunch() {
        return (getValue() & CAN_PUNCH) == 1;
    }
    
    public boolean canCover() {
        return (getValue() & CAN_COVER) == 1;
    }
    
    public boolean canBind() {
        return (getValue() & CAN_BIND) == 1;
    }
    
    public boolean canSort() {
        return (getValue() & CAN_SORT) == 1;
    }
    
    public boolean canA4() {
        return (getValue() & CAN_A4) == 1;
    }
    
    public boolean canA2() {
        return (getValue() & CAN_A2) == 1;
    }
    public boolean canLargerA2() {
        return (getValue() & CAN_LARGER_A2) == 1;
    }
    
    public boolean canUserDefinedMediaSize() {
        return (getValue() & CAN_USER_DEFINED_MEDIA_SIZE) == 1;
    }
    public boolean isImplicitClass() {
        return (getValue() & IS_IMPLICIT_CLASS) == 1;
    }

    
    /**
     * @param value
     */
    public PrinterType(int value) {
        super(value);
    }

    /**
     *
     */

    public Class getCategory() {
        return this.getClass();
    }

    /**
     *
     */

    public String getName() {
        return PrinterType.getIppName();
    }

    /**
     * 
     */
    public static final String getIppName() {
        return "printer-type";
    }

}
