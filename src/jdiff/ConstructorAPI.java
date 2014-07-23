package jdiff;

import java.io.*;
import java.util.*;

/** 
 * Class to represent a constructor, analogous to ConstructorDoc in the 
 * Javadoc doclet API. 
 *
 * The method used for Collection comparison (compareTo) must make its
 * comparison based upon everything that is known about this constructor.
 *
 * See the file LICENSE.txt for copyright details.
 * @author Matthew Doar, mdoar@pobox.com
 */
class ConstructorAPI implements Comparable {
    /** Name of the constructor dd. */
    public String name_ = null;

    /** 
     * The type of the constructor, being all the parameter types
     * separated by commas.
     */
    public String type_ = null;
    
    /** 
     * The exceptions thrown by this constructor, being all the exception types
     * separated by commas. "no exceptions" if no exceptions are thrown.
     */
    public String exceptions_ = "no exceptions";
    
    /** Modifiers for this class. */
    public Modifiers modifiers_;

    public List params_; // ParamAPI[]

    /** The doc block, default is null. */
    public String doc_ = null;

    /** Constructor. */
    public ConstructorAPI(String name, String type, Modifiers modifiers) {
        name_ = name;
        type_ = type; // in android, holds fully qualified name -- not used here
        modifiers_ = modifiers;
        params_ = new ArrayList(); // ParamAPI[]
    }

//dbd Comment out type-based comparison
    /** Compare two ConstructorAPI objects by type and modifiers. 
    public int compareTo(Object o) {
        ConstructorAPI constructorAPI = (ConstructorAPI)o;
        int comp = type_.compareTo(constructorAPI.type_);
        if (comp != 0)
            return comp;
        comp = getSignature().compareTo(constructorAPI.getSignature());
        if (comp != 0)
            return comp;
        comp = exceptions_.compareTo(constructorAPI.exceptions_);
        if (comp != 0)
            return comp;
        comp = modifiers_.compareTo(constructorAPI.modifiers_);
        if (comp != 0)
            return comp;
        if (APIComparator.docChanged(doc_, constructorAPI.doc_))
            return -1;
        return 0;
    }

*/
    /** 
     * Compare two ConstructorAPI objects dbd 
     */
    public int compareTo(Object o) {
        ConstructorAPI constructorAPI = (ConstructorAPI)o;
        int comp = name_.compareTo(constructorAPI.name_);
        if (comp != 0) 
            return comp;
        comp = modifiers_.compareTo(constructorAPI.modifiers_);
        if (comp != 0) 
            return comp;
        comp = getSignature().compareTo(constructorAPI.getSignature());
        if (comp != 0) 
            return comp; 
        if (APIComparator.docChanged(doc_, constructorAPI.doc_)) 
            return -1; 
        return 0;
    }



     /** Tests two constructors for equality, using just the signature.
     */
    public boolean equals(Object o) {
        if (getSignature().compareTo(((ConstructorAPI)o).getSignature()) == 0)
            return true;
        return false;
    }



//dbd Comment out type-based comparison
    /** 
     * Tests two constructors, using just the type, used by indexOf(). 
    
    public boolean equals(Object o) {
        if (type_.compareTo(((ConstructorAPI)o).type_) == 0)
            return true;
        return false;
    }
 */

    /** Cached result of getSignature(). dbd  */
    public String signature_ = null;
    /** Return the signature of the constructor. dbd */
    public String getSignature() {
        if (signature_ != null)
            return signature_;
        String res = "";
        boolean first = true;
        Iterator iter = params_.iterator();
        while (iter.hasNext()) {
            if (!first)
                res += ", ";
            ParamAPI param = (ParamAPI)(iter.next());
            res += param.toString();
            first = false;
        }
        signature_ = res;
//dbd System.out.println("SSSSSSS: Signature is " + res);
        return res; 
    }
}  
