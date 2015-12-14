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
package de.lohndirekt.print;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.print.Doc;
import javax.print.MultiDoc;

/**
 * @author bpusch
 *
 */
public class SimpleMultiDoc implements MultiDoc {

    private List listeners;
    Doc doc = null;
    boolean lastDoc = false;
    SimpleMultiDoc next = null;

    public static SimpleMultiDoc create(Doc doc) {
        return new SimpleMultiDoc(doc, false);
    }

    public static SimpleMultiDoc create(Doc doc, boolean lastDoc) {
        return new SimpleMultiDoc(doc, lastDoc);
    }

    /**
     * 
     */
    protected SimpleMultiDoc(Doc doc, boolean lastDoc) {
        if (doc == null) {
            throw new NullPointerException("Doc must not be null");
        }
        this.doc = doc;
        this.lastDoc = lastDoc;
    }

    /**
     * Adds a new MultiDoc.
     * If the next MultiDoc has already been set, it tries to add it there
     * 
     * @param doc
     * @param lastDoc
     * @throws IllegalStateException if this has already been the last Document
     */
    public void setNext(Doc doc, boolean lastDoc) {
        if (this.lastDoc) {
            throw new IllegalStateException("Last Doc already sent");
        }
        if (doc == null) {
            throw new NullPointerException("Doc must not be null");
        }
        if (next == null) {
            this.next = new SimpleMultiDoc(doc, lastDoc);
        } else {
            this.next.setNext(doc, lastDoc);
        }
        this.notifyListeners();
    }

    public void setNext(Doc doc) {
        setNext(doc, false);
    }

    /**
     *
     */

    public Doc getDoc() throws IOException {
        return this.doc;
    }

    /**
     *
     */
    public MultiDoc next() throws IOException {
        if (!this.available()) {
            throw new IllegalStateException("Next MultiDoc is not yet available");
        }
        return this.next;
    }

    public boolean available() {
        return (!this.lastDoc && this.next != null);
    }

    public boolean isLast() {
        return lastDoc;
    }

    public String toString() {
        return this.getClass() + " - " + this.doc.toString();
    }

    public void addMultiDocListener(MultiDocListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList();
        }
        this.listeners.add(listener);
    }

    private void notifyListeners() {
        if (this.listeners != null) {
            for (Iterator iter = this.listeners.iterator(); iter.hasNext();) {
                MultiDocListener listener = (MultiDocListener) iter.next();
                try {
                    listener.processEvent(new MultiDocEvent(this.next));
                } catch (IOException e) {
                    //TODO handle exception appropriatly
                }
            }
        }
    }
}
