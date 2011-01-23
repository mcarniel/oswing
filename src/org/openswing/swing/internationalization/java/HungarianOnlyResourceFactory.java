package org.openswing.swing.internationalization.java;

import java.util.*;
import org.openswing.swing.util.java.Consts;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieve the collection of all internationalization properties:
 * translations, data/numeric/currency formats.
 * No translation is performed, date/numeric/currency formats are based on english formats.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel/Zolt\u00E1n Zidarics/Attila Szomor
 * @version 1.0
 */
public class HungarianOnlyResourceFactory extends ResourcesFactory {

    /** internationalization settings */
    private Resources resources = null;

    /**
     * Constructor.
     * @param currencySymbol currency symbol
     * @param additionalDictionary additional descriptions
     * @param showResourceNotFoundWarning warn when no resource key not found
     */
    public HungarianOnlyResourceFactory(String currencySymbol, Properties additionalDictionary, boolean showResourceNotFoundWarning) {
        this(currencySymbol, additionalDictionary, showResourceNotFoundWarning, '/');
    }

    /**
     * Constructor.
     * @param currencySymbol currency symbol
     * @param additionalDictionary additional descriptions
     * @param showResourceNotFoundWarning warn when no resource key not found
     * @param dateFormatSeparator date format separator; for example: '-' or '/'
     */
    public HungarianOnlyResourceFactory(String currencySymbol, Properties additionalDictionary, boolean showResourceNotFoundWarning, char dateFormatSeparator) {
        Properties dictionary = new Properties();

        dictionary.putAll(additionalDictionary);

        // grid...
        dictionary.setProperty("of", "/");
        dictionary.setProperty("page", "Oldal");
        dictionary.setProperty("Remove Filter", "Sz\u0171r\u0151felt\u00E9tel t\u00F6rl\u00E9se");
        dictionary.setProperty("This column is not sorteable", "Ezen oszlop alapj\u00E1n nem rendezhet\u0151");
        dictionary.setProperty("Sorting not allowed", "A rendez\u00E9s nem enged\u00E9lyezett");
        dictionary.setProperty("Maximum number of sorted columns", "Maxim\u00E1lisan rendezhet\u0151 oszlopok sz\u00E1ma");
        dictionary.setProperty("Sorting not applicable", "Rendez\u00E9s nem hajthat\u00F3 v\u00E9cgre");
        dictionary.setProperty("Selected Row", "Kijel\u00F6lt sor");
        dictionary.setProperty("Selected Rows", "Kijel\u00F6lt sorok");
        dictionary.setProperty("Cancel changes and reload data?", "Eldobja a v\u00E1ltozt\u00E1sokat \u00E9s \u00FAjrat\u00F6lti az adatokat?");
        dictionary.setProperty("Attention", "Figyelmeztet\u00E9s");
        dictionary.setProperty("Loading data...", "Adatok bet\u00F6lt\u00E9se...");
        dictionary.setProperty("Error while loading data", "Hiba bet\u00F6lt\u00E9s k\u00F6zben");
        dictionary.setProperty("Loading Data Error", "Adathiba");
        dictionary.setProperty("Delete Rows?", "Sor t\u00F6rl\u00E9se?");
        dictionary.setProperty("Delete Confirmation", "T\u00F6rl\u00E9s meger\u0151s\u00EDt\u00E9s");
        dictionary.setProperty("Error while deleting rows.", "Hiba sor t\u00F6rl\u00E9se k\u00F6zben.");
        dictionary.setProperty("Deleting Error", "T\u00F6rl\u00E9s hiba");
        dictionary.setProperty("Error while saving", "Hiba ment\u00E9s k\u00F6zben");
        dictionary.setProperty("Saving Error", "Ment\u00E9s hiba");
        dictionary.setProperty("A mandatory column is empty.", "A k\u00F6telez\u0151en kit\u00F6ltend\u0151 oszlop \u00FCres.");
        dictionary.setProperty("Value not valid", "Hib\u00E1s \u00E9rt\u00E9k");
        dictionary.setProperty("sorting conditions", "Rendez\u00E9si felt\u00E9telek");
        dictionary.setProperty("filtering conditions", "Sz\u0171r\u00E9si felt\u00E9telek");
        dictionary.setProperty("filtering and sorting settings", "Sz\u0171r\u00E9si \u00E9s rendez\u00E9si be\u00E1ll\u00EDt\u00E1sok");
        dictionary.setProperty("Filtering/Sorting data (CTRL+F)", "Adat sz\u0171r\u00E9s/rendez\u00E9s (CTRL+F)");
        dictionary.setProperty("upload file", "F\u00E1jl felt\u00F6lt\u00E9se");
        dictionary.setProperty("download file", "F\u00E1jl let\u00F6lt\u00E9se");

        // export...
        dictionary.setProperty("grid export", "T\u00E1bla export\u00E1l\u00E1s");
        dictionary.setProperty("export", "Export\u00E1l\u00E1s");
        dictionary.setProperty("exportmnemonic", "X");
        dictionary.setProperty("column", "Oszlop");
        dictionary.setProperty("sel.", "Jel\u00F6l");
        dictionary.setProperty("you must select at least one column", "Legal\u00E1bb egy oszlopot ki kell jel\u00F6lnie");
        dictionary.setProperty("columns to export", "Export\u00E1land\u00F3 oszlopok");
        dictionary.setProperty("export type", "Export form\u00E1tum");

        // import...
        dictionary.setProperty("grid import", "T\u00E1bla Import\u00E1l\u00E1s");
        dictionary.setProperty("file to import", "Import file");
        dictionary.setProperty("import", "Import");
        dictionary.setProperty("importmnemonic", "M");
        dictionary.setProperty("columns to import", "Import\u00E1land\u00F3 oszlopok");
        dictionary.setProperty("import type", "Import form\u00E1tum");
        dictionary.setProperty("error while importing data", "Hiba import\u00E1l\u00E1s k\u00F6zben");
        dictionary.setProperty("import completed", "Import\u00E1l\u00E1s k\u00E9sz");

        // quick filter...
        dictionary.setProperty("To value", "Tartalomra");
        dictionary.setProperty("Filter by", "Sz\u0171r\u00E9s alapja");
        dictionary.setProperty("From value", "\u00C9rt\u00E9kt\u0151l");
        dictionary.setProperty("equals", "egyenl\u0151");
        dictionary.setProperty("contains", "tartalmazza");
        dictionary.setProperty("starts with", "kezd\u0151dik");
        dictionary.setProperty("ends with", "v\u00E9gz\u0151dik");

        // select/deselect all
        dictionary.setProperty("select all", "Az \u00F6sszes kiejl\u00F6l\u00E9se");
        dictionary.setProperty("deselect all", "Kijel\u00F6l\u00E9sek megsz\u00FCntet\u00E9se");

        // copy/cut/paste
        dictionary.setProperty("copy", "M\u00E1sol");
        dictionary.setProperty("copymnemonic", "C");
        dictionary.setProperty("cut", "Kiv\u00E1g");
        dictionary.setProperty("cutmnemonic", "X");
        dictionary.setProperty("paste", "Beilleszt");
        dictionary.setProperty("pastemnemonic", "V");

        // lookup...
        dictionary.setProperty("Code is not correct.", "A k\u00F3d nem korrekt");
        dictionary.setProperty("Code Validation", "K\u00F3d valid\u00E1l\u00E1s");
        dictionary.setProperty("Code Selection", "K\u00F3d v\u00E1laszt\u00E1s");

        // form...
        dictionary.setProperty("Confirm deliting data?", "Biztos t\u00F6r\u00F6lni akarja?");
        dictionary.setProperty("Error while saving: incorrect data.", "Hiba ment\u00E9s k\u00F6zben: inkorrekt adat.");
        dictionary.setProperty("Error while validating data:","Hiba adatellen\u0151rz\u00E9s k\u00F6zben:");
        dictionary.setProperty("Validation Error","Ellen\u0151rz\u00E9si hiba");
        dictionary.setProperty("Error on deleting:", "Hiba t\u00F6rl\u00E9s k\u00F6zben:");
        dictionary.setProperty("Error on Loading", "Hiba bet\u00F6lt\u00E9s k\u00F6zben");
        dictionary.setProperty("Error while loading data:", "Hiba bet\u00F6lt\u00E9s k\u00F6zben:");
        dictionary.setProperty("Error on setting value to the input control having the attribute name", "Hiba be\u00E1ll\u00EDt\u00E1s k\u00F6zben az attributumnak nincs \u00E9rt\u00E9ke");

        // toolbar buttons...
        dictionary.setProperty("Delete record (CTRL+D)", "Rekord t\u00F6rl\u00E9s (CTRL+D)");
        dictionary.setProperty("Edit record (CTRL+E)", "Rekord szerkeszt\u00E9se (CTRL+E)");
        dictionary.setProperty("New record (CTRL+I)", "\u00DAj rekord (CTRL+I)");
        dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)", "Az aktu\u00E1lis oper\u00E1ci\u00F3 t\u00F6rl\u00E9se/rekord \u00FAjrat\u00F6lt\u00E9se (CTRL+Z)");
        dictionary.setProperty("Save record (CTRL+S)", "Rekord ment\u00E9se (CTRL+S)");
        dictionary.setProperty("Copy record (CTRL+C)", "Rekord m\u00E1sol\u00E1sa (CTRL+C)");
        dictionary.setProperty("Export record (CTRL+X)", "Rekord export\u00E1l\u00E1sa (CTRL+X)");
        dictionary.setProperty("Import records (CTRL+M)", "Rekords import\u00E1l\u00E1sa (CTRL+M)");
        dictionary.setProperty("Load the first block of records", "Az els\u0151 blokk bet\u00F6lt\u00E9se");
        dictionary.setProperty("Select the previous row in grid", "A t\u00E1bla el\u0151z\u0151 sor\u00E1nak kiv\u00E1laszt\u00E1sa");
        dictionary.setProperty("Select the next row in grid", "A t\u00E1bla k\u00F6vetkez\u0151 sor\u00E1nak kiv\u00E1laszt\u00E1sa");
        dictionary.setProperty("Load the previous block of records", "Az el\u0151z\u0151 blokk bet\u00F6lt\u00E9se");
        dictionary.setProperty("Load the next block of records", "A k\u00F6vetkez\u0151 blokk bet\u00F6lt\u00E9se");
        dictionary.setProperty("Load the last block of records", "Az utols\u00F3 blokk bet\u00F6lt\u00E9se");

        dictionary.setProperty("Insert", "Besz\u00FAr\u00E1s");
        dictionary.setProperty("Edit", "Szerkeszt\u00E9s");
        dictionary.setProperty("Copy", "M\u00E1sol\u00E1s");
        dictionary.setProperty("Delete", "T\u00F6rl\u00E9s");
        dictionary.setProperty("Save", "Ment\u00E9s");
        dictionary.setProperty("Reload", "\u00DAjrat\u00F6lt\u00E9s");
        dictionary.setProperty("Export", "Export");
        dictionary.setProperty("Filter", "Sz\u0171r\u00E9s");

        // MDI Frame...
        dictionary.setProperty("file", "F\u00E1jl");
        dictionary.setProperty("exit", "Kil\u00E9p\u00E9s");
        dictionary.setProperty("filemnemonic", "F");
        dictionary.setProperty("exitmnemonic", "K");
        dictionary.setProperty("change user", "Felhaszn\u00E1l\u00F3 v\u00E1lt\u00E1s");
        dictionary.setProperty("changeusermnemonic", "V");
        dictionary.setProperty("changelanguagemnemonic", "L");
        dictionary.setProperty("help", "S\u00FAg\u00F3");
        dictionary.setProperty("about", "N\u00E9vjegy");
        dictionary.setProperty("helpmnemonic", "S");
        dictionary.setProperty("aboutmnemonic", "N");
        dictionary.setProperty("are you sure to quit application?", "Biztosan kil\u00E9p?");
        dictionary.setProperty("quit application", "Kil\u00E9p\u00E9s");
        dictionary.setProperty("forcegcmnemonic", "F");
        dictionary.setProperty("Force GC", "Tisztogat\u00E1s");
        dictionary.setProperty("Java Heap", "Java Heap");
        dictionary.setProperty("used", "haszn\u00E1lt");
        dictionary.setProperty("allocated", "allok\u00E1lva");
        dictionary.setProperty("change language", "Nyelv v\u00E1lt\u00E1s");
        dictionary.setProperty("changemnemonic", "N");
        dictionary.setProperty("cancelmnemonic", "C");
        dictionary.setProperty("cancel", "Eldob");
        dictionary.setProperty("yes", "Igen");
        dictionary.setProperty("no", "Nem");
        dictionary.setProperty("Functions", "Funkci\u00F3k");
        dictionary.setProperty("Error while executing function", "Hiba a funkci\u00F3 v\u00E9grehajt\u00E1sa k\u00F6zben");
        dictionary.setProperty("Error", "Hiba");
        dictionary.setProperty("infoPanel", "Info");
        dictionary.setProperty("imageButton", "N\u00E9vjegy");
        dictionary.setProperty("Window", "Ablak");
        dictionary.setProperty("windowmnemonic", "W");
        dictionary.setProperty("Close All", "Bez\u00E1r mindent");
        dictionary.setProperty("closeallmnemonic", "A");
        dictionary.setProperty("closemnemonic","A");
        dictionary.setProperty("Press ENTER to find function", "ENTER a funkci\u00F3 keres\u00E9shez");
        dictionary.setProperty("Find Function", "Funkci\u00F3 keres\u00E9s");
        dictionary.setProperty("Operation in progress...", "Oper\u00E1ci\u00F3 v\u00E9grehajt\u00E1s alatt...");
        dictionary.setProperty("close window", "Ablak bez\u00E1r\u00E1s");
        dictionary.setProperty("reduce to icon", "Ikonm\u00E9ret");
        dictionary.setProperty("save changes?", "V\u00E1ltoz\u00E1sok elment\u00E9se?");
        dictionary.setProperty("confirm window closing", "Ablak bez\u00E1r\u00E1s meger\u0151s\u00EDt\u00E9se");
        dictionary.setProperty("change background", "H\u00E1tt\u00E9r csere");
        dictionary.setProperty("reset background", "H\u00E1tt\u00E9r friss\u00EDt\u00E9s");

        dictionary.setProperty("switch", "V\u00E1lt\u00E1s");
        dictionary.setProperty("switchmnemonic", "S");
        dictionary.setProperty("window name", "Ablak neve");
        dictionary.setProperty("opened windows", "Nyitott ablakok");
        dictionary.setProperty("tile horizontally", "V\u00EDzszintes elrendez\u00E9s");
        dictionary.setProperty("tilehorizontallymnemonic", "H");
        dictionary.setProperty("tile vertically", "F\u00FCgg\u0151leges elrendez\u00E9s");
        dictionary.setProperty("tileverticallymnemonic", "V");
        dictionary.setProperty("cascade", "Lez\u00FAdul\u00F3 elrendez\u00E9s");
        dictionary.setProperty("cascademnemonic", "C");
        dictionary.setProperty("minimize", "Kis m\u00E9ret");
        dictionary.setProperty("minimizemnemonic", "M");
        dictionary.setProperty("minimize all", "Mind kis m\u00E9retre");
        dictionary.setProperty("minimizeallmnemonic", "A");
        dictionary.setProperty("selected frame","kijel\u00F6lt ablak");

        // server...
        dictionary.setProperty("Client request not supported", "A k\u00E9r\u00E9s kiszolg\u00E1l\u00E1sa nem megval\u00F3s\u00EDtott");
        dictionary.setProperty("User disconnected", "Felhaszn\u00E1l\u00F3 ki\u00E9cpett");
        dictionary.setProperty("Updating not performed: the record was previously updated.", "A m\u00F3dos\u00EDt\u00E1s nem siker\u00FClt: a rekordot el\u0151z\u0151leg m\u00F3dos\u00EDtott\u00E1k.");

        // wizard...
        dictionary.setProperty("back", "Vissza");
        dictionary.setProperty("next", "K\u00F6vetkez\u0151");
        dictionary.setProperty("finish", "V\u00E9ge");

        // image panel...
        dictionary.setProperty("image selection", "K\u00E9p v\u00E1laszt\u00E1s");

        // tip of the day panel...
        dictionary.setProperty("show 'tip of the day' after launching", "Programind\u00EDt\u00E1skor napi tippek");
        dictionary.setProperty("previous tip", "El\u0151z\u0151 tipp");
        dictionary.setProperty("next tip", "K\u00F6vetkez\u0151 tipp");
        dictionary.setProperty("close", "Bez\u00E1r");
        dictionary.setProperty("tip of the day", "Napi tippek");
        dictionary.setProperty("select tip", "Tipp kiv\u00E1laszt\u00E1s");
        dictionary.setProperty("tip name", "Tipp neve");
        dictionary.setProperty("tips list", "Tippek list\u00E1ja");

        // progress panel...
        dictionary.setProperty("progress", "Folyamatban");

        // licence agreement...
        dictionary.setProperty("i accept the terms in the licence agreement", "Elfogadom a licensz-szerz\u0151d\u00E9sben foglaltakat");
        dictionary.setProperty("ok", "Rendben");
        dictionary.setProperty("i do not accept the terms in the licence agreement", "Nem fogadom el a licensz-szerz\u0151d\u00E9sben foglaltakat");

        // property grid control
        dictionary.setProperty("property name", "N\u00E9v");
        dictionary.setProperty("property value", "\u00C9rt\u00E9k");

        // grid profile
        dictionary.setProperty("grid profile management", "T\u00E1bla profil management");
        dictionary.setProperty("restore default grid profile", "Az alap\u00E9rtelmezett t\u00E1blaprofil visszat\u00F6lt\u00E9se");
        dictionary.setProperty("create new grid profile", "\u00DAj t\u00E1blaprofil");
        dictionary.setProperty("profile description", "Profil le\u00EDr\u00E1s");
        dictionary.setProperty("remove current grid profile", "T\u00E1blaprofil t\u00F6rl\u00E9se");
        dictionary.setProperty("select grid profile", "T\u00E1blaprofil v\u00E1laszt\u00E1s");
        dictionary.setProperty("default profile", "Alap\u00E9rtelmezett profil");

        // search box
        dictionary.setProperty("search", "Keres\u00E9s");
        dictionary.setProperty("not found", "Nem tal\u00E1lhat\u00F3");

        // drag...
        dictionary.setProperty("drag", "Drag");

        // pivot table...
        dictionary.setProperty("pivot table settings", "Pivot t\u00E1bla be\u00E1ll\u00EDt\u00E1sok");
        dictionary.setProperty("row fields", "Sor mez\u0151k");
        dictionary.setProperty("column fields", "Oszlop mez\u0151k");
        dictionary.setProperty("data fields", "Adat mez\u0151k");
        dictionary.setProperty("filtering conditions", "Sz\u0171r\u00E9si felt\u00E9telek");
        dictionary.setProperty("field", "Mez\u0151");
        dictionary.setProperty("checked", "Kijel\u00F6lt");
        dictionary.setProperty("at least one data field must be selected", "Minimum egy adat mez\u0151t k\u00F6telez\u0151 v\u00E1lasztani.");
        dictionary.setProperty("at least one row field must be selected", "Minimum egy sor mez\u0151t k\u00F6telez\u0151 v\u00E1lasztani.");
        dictionary.setProperty("at least one column field must be selected", "Minimum egy oszlop mez\u0151t k\u00F6telez\u0151 v\u00E1lasztani.");
        dictionary.setProperty("expand all", "Felfedi mind");
        dictionary.setProperty("collapse all", "Elrejti mind");

        dictionary.setProperty("Caps lock pressed","Caps lock nyomva");

        // LoginDialog
        dictionary.setProperty("Logon", "Bejelentkez\u00E9s");
        dictionary.setProperty("Login", "Bel\u00E9p\u00E9s");
        dictionary.setProperty("L", "B");
        dictionary.setProperty("Exit", "Kil\u00E9p\u00E9s");
        dictionary.setProperty("E", "K");
        dictionary.setProperty("store account", "Elments\u00FCk a felhaszn\u00E1l\u00F3t");
        dictionary.setProperty("Username", "Felhaszn\u00E1l\u00F3");
        dictionary.setProperty("Password", "Jelsz\u00F3");

        dictionary.setProperty(Consts.EQ,"Equals to");
        dictionary.setProperty(Consts.GE,"Greater or equals to");
        dictionary.setProperty(Consts.GT,"Greater than");
        dictionary.setProperty(Consts.IS_NOT_NULL,"Is filled");
        dictionary.setProperty(Consts.IS_NULL,"Is not filled");
        dictionary.setProperty(Consts.LE,"Less or equals to");
        dictionary.setProperty(Consts.LIKE,"Contains");
        dictionary.setProperty(Consts.LT,"Less than");
        dictionary.setProperty(Consts.NEQ,"Not equals to");
        dictionary.setProperty(Consts.IN,"Contains values");
        dictionary.setProperty(Consts.ASC_SORTED,"Ascending");
        dictionary.setProperty(Consts.DESC_SORTED,"Descending");
        dictionary.setProperty(Consts.NOT_IN,"Not contains values");

        resources = new Resources(
                dictionary,
                currencySymbol,
                '.',
                ',',
                Resources.YMD,
                true,
                dateFormatSeparator,
                "HH:mm",
                "HU",
                showResourceNotFoundWarning);
    }

    /**
     * @return internationalization settings, according with the current language
     */
    public final Resources getResources() {
        return resources;
    }

    /**
     * Load dictionary, according to the specified language id.
     * @param langId language id identifier
     */
    public final void setLanguage(String langId) throws UnsupportedOperationException {
        if (!resources.getLanguageId().equals(langId)) {
            throw new UnsupportedOperationException("Nem t\u00E1mogatott nyelv.");
        }
    }

    /**
     * @param langId language id identifier
     * @return internationalization settings, according with the language specified
     */
    public final Resources getResources(String langId) throws UnsupportedOperationException {
        if (!resources.getLanguageId().equals(langId)) {
            throw new UnsupportedOperationException("Nem t\u00E1mogatott nyelv.");
        }
        return resources;
    }
}




