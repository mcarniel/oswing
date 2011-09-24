package org.openswing.swing.internationalization.java;

import java.util.*;
import org.openswing.swing.util.java.Consts;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieve the collection of all internationalization properties:
 * translations, data/numeric/currency formats.
 * No translation is performed, date/numeric/currency formats are based on Czech formats.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel / 2009 Lukas Voves</p>
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
 *           maurocarniel@tin.it
 *           kiwi.dela.divy@gmail.com</p>
 *
 * @author Mauro Carniel and Lukas Voves (CzechOnlyResourceFactory)
 * @version 1.0
 */
public class CzechOnlyResourceFactory extends ResourcesFactory {

    /** internationalization settings */
    private Resources resources = null;

    /**
     * Constructor.
     * @param currencySymbol currency symbol
     * @param additionalDictionary additional descriptions
     * @param showResourceNotFoundWarning warn when no resource key not found
     */
    public CzechOnlyResourceFactory(String currencySymbol, Properties additionalDictionary, boolean showResourceNotFoundWarning) {
        this(currencySymbol, additionalDictionary, showResourceNotFoundWarning, '.');
    }

    /**
     * Constructor.
     * @param currencySymbol currency symbol
     * @param additionalDictionary additional descriptions
     * @param showResourceNotFoundWarning warn when no resource key not found
     * @param dateFormatSeparator date format separator; for example: '.' or '/'
     */
    public CzechOnlyResourceFactory(String currencySymbol, Properties additionalDictionary, boolean showResourceNotFoundWarning, char dateFormatSeparator) {
        Properties dictionary = new Properties();

        dictionary.putAll(additionalDictionary);

        // grid...
        dictionary.setProperty("of", "z");
        dictionary.setProperty("page", "Str\u00E1nka");
        dictionary.setProperty("Remove Filter", "Odstranit Filtr");
        dictionary.setProperty("This column is not sorteable", "Tento sloupec nelze t\u0159\u00EDdit");
        dictionary.setProperty("Sorting not allowed", "T\u0159\u00EDd\u011Bn\u00ED je zak\u00E1z\u00E1no");
        dictionary.setProperty("Maximum number of sorted columns", "Maxim\u00E1ln\u00ED po\u010Det t\u0159\u00EDd\u011Bn\u00FDch sloupc\u016F");
        dictionary.setProperty("Sorting not applicable", "T\u0159\u00EDd\u011Bn\u00ED nelze aplikovat");
        dictionary.setProperty("Selected Row", "Vybran\u00FD \u0159\u00E1dek");
        dictionary.setProperty("Selected Rows", "Vybran\u00E9 \u0159\u00E1dky");
        dictionary.setProperty("Cancel changes and reload data?", "Zru\u0161it zm\u011Bny a obnovit data?");
        dictionary.setProperty("Attention", "Pozor");
        dictionary.setProperty("Loading data...", "Nahr\u00E1v\u00E1m data...");
        dictionary.setProperty("Error while loading data", "B\u011Bhem nahr\u00E1v\u00E1n\u00ED dat nastala chyba");
        dictionary.setProperty("Loading Data Error", "Chyba nahr\u00E1v\u00E1n\u00ED dat");
        dictionary.setProperty("Delete Rows?", "Smazat \u0159\u00E1dky?");
        dictionary.setProperty("Delete Confirmation", "Potvrzen\u00ED smaz\u00E1n\u00ED");
        dictionary.setProperty("Error while deleting rows.", "B\u011Bhem maz\u00E1n\u00ED \u0159\u00E1dk\u016F nastala chyba.");
        dictionary.setProperty("Deleting Error", "Chyba maz\u00E1n\u00ED");
        dictionary.setProperty("Error while saving", "B\u011Bhem ukl\u00E1d\u00E1n\u00ED nastala chyba");
        dictionary.setProperty("Saving Error", "Chyba ukl\u00E1d\u00E1n\u00ED");
        dictionary.setProperty("A mandatory column is empty.", "Povinn\u00FD sloupec je pr\u00E1zdn\u00FD");
        dictionary.setProperty("Value not valid", "Hodnota nen\u00ED platn\u00E1");
        dictionary.setProperty("sorting conditions", "Podm\u00EDnky t\u0159\u00EDd\u011Bn\u00ED");
        dictionary.setProperty("filtering conditions", "Podm\u00EDnky filtrov\u00E1n\u00ED");
        dictionary.setProperty("filtering and sorting settings", "Nastaven\u00ED filtrov\u00E1n\u00ED a t\u0159\u00EDd\u011Bn\u00ED");
        dictionary.setProperty("Filtering/Sorting data (CTRL+F)", "Filtrov\u00E1n\u00ED/T\u0159\u00EDd\u011Bn\u00ED dat (CTRL+F)");
        dictionary.setProperty("upload file", "Nahr\u00E1t soubor");
        dictionary.setProperty("download file", "St\u00E1hnout soubor");

        // export...
        dictionary.setProperty("grid export", "Export tabulky");
        dictionary.setProperty("export", "Export");
        dictionary.setProperty("exportmnemonic", "X");
        dictionary.setProperty("column", "Sloupec");
        dictionary.setProperty("sel.", "V\u00FDb\u011Br");
        dictionary.setProperty("you must select at least one column", "Mus\u00EDte vybrat alespo\u0148 jeden sloupec");
        dictionary.setProperty("columns to export", "Sloupce pro export");
        dictionary.setProperty("export type", "Form\u00E1t exportu");

        // import...
        dictionary.setProperty("grid import", "Import tabulky");
        dictionary.setProperty("file to import", "Soubor pro import");
        dictionary.setProperty("import", "Importovat");
        dictionary.setProperty("importmnemonic", "M");
        dictionary.setProperty("columns to import", "Sloupce pro import");
        dictionary.setProperty("import type", "Form\u00E1t importu");
        dictionary.setProperty("error while importing data", "P\u0159i importov\u00E1n\u00ED dat nastala chyba");
        dictionary.setProperty("import completed", "Import dokon\u010Den.");

        // quick filter...
        dictionary.setProperty("To value", "Do hodnoty");
        dictionary.setProperty("Filter by", "Filtrovat podle");
        dictionary.setProperty("From value", "Od hodnoty");
        dictionary.setProperty("equals", "rovn\u00E1 se");
        dictionary.setProperty("contains", "obsahuje");
        dictionary.setProperty("starts with", "za\u010D\u00EDn\u00E1 na");
        dictionary.setProperty("ends with", "kon\u010D\u00ED na");

        // select/deselect all
        dictionary.setProperty("select all", "Vybrat v\u0161e");
        dictionary.setProperty("deselect all", "Zru\u0161it v\u00FDb\u011Br");

        // copy/cut/paste
        dictionary.setProperty("copy", "Kop\u00EDrovat");
        dictionary.setProperty("copymnemonic", "C");
        dictionary.setProperty("cut", "Vyjmout");
        dictionary.setProperty("cutmnemonic", "U");
        dictionary.setProperty("paste", "Vlo\u017Eit");
        dictionary.setProperty("pastemnemonic", "P");

        // lookup...
        dictionary.setProperty("Code is not correct.", "K\u00F3d je \u0161patn\u00FD.");
        dictionary.setProperty("Code Validation", "Ov\u011B\u0159en\u00ED k\u00F3du");
        dictionary.setProperty("Code Selection", "V\u00FDb\u011Br k\u00F3du");

        dictionary.setProperty("Caps lock pressed","Tla\u010D\u00EDtko Caps lock je stisknuto");

        // form...
        dictionary.setProperty("Confirm deliting data?", "Potvrdit smaz\u00E1n\u00ED dat?");
        dictionary.setProperty("Error while saving: incorrect data.", "Chyba p\u0159i ukl\u00E1d\u00E1n\u00ED: \u0161patn\u00E1 data.");
        dictionary.setProperty("Error while validating data:", "Chyba p\u0159i ov\u011B\u0159ov\u00E1n\u00ED dat:");
        dictionary.setProperty("Validation Error", "Chyba ov\u011B\u0159ov\u00E1n\u00ED");
        dictionary.setProperty("Error on deleting:", "Chyba p\u0159i maz\u00E1n\u00ED:");
        dictionary.setProperty("Error on Loading", "Chyba p\u0159i nahr\u00E1v\u00E1n\u00ED");
        dictionary.setProperty("Error while loading data:", "Chyba p\u0159i nahr\u00E1v\u00E1n\u00ED dat:");
        dictionary.setProperty("Error on setting value to the input control having the attribute name", "Chyba p\u0159i nastavov\u00E1n\u00ED hodnoty pro kontrolu vkl\u00E1d\u00E1n\u00ED, kter\u00E9 m\u00E1 n\u00E1zev atributu");

        // toolbar buttons...
        dictionary.setProperty("Delete record (CTRL+D)", "Smazat z\u00E1znam (CTRL+D)");
        dictionary.setProperty("Edit record (CTRL+E)", "Upravit z\u00E1znam (CTRL+E)");
        dictionary.setProperty("New record (CTRL+I)", "Nov\u00FD z\u00E1znam (CTRL+I)");
        dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)", "Obnovit z\u00E1znam/Zru\u0161it sou\u010Dasnou operaci (CTRL+Z)");
        dictionary.setProperty("Save record (CTRL+S)", "Ulo\u017Eit z\u00E1znam (CTRL+S)");
        dictionary.setProperty("Copy record (CTRL+C)", "Kop\u00EDrovat z\u00E1znam (CTRL+C)");
        dictionary.setProperty("Export record (CTRL+X)", "Exportovat z\u00E1znamy (CTRL+X)");
        dictionary.setProperty("Import records (CTRL+M)", "Importovat z\u00E1znamy (CTRL+M)");
        dictionary.setProperty("Load the first block of records", "Nahr\u00E1t prvn\u00ED blok z\u00E1znam\u016F");
        dictionary.setProperty("Select the previous row in grid", "Vybrat p\u0159edchoz\u00ED \u0159\u00E1dek v tabulce");
        dictionary.setProperty("Select the next row in grid", "Vybrat dal\u0161\u00ED \u0159\u00E1dek v tabulce");
        dictionary.setProperty("Load the previous block of records", "Nahr\u00E1t p\u0159edchoz\u00ED blok z\u00E1znam\u016F");
        dictionary.setProperty("Load the next block of records", "Nahr\u00E1t n\u00E1sleduj\u00EDc\u00ED blok z\u00E1znam\u016F");
        dictionary.setProperty("Load the last block of records", "Nahr\u00E1t posledn\u00ED blok z\u00E1znam\u016F");

        dictionary.setProperty("Insert", "Vlo\u017Eit");
        dictionary.setProperty("Edit", "Upravit");
        dictionary.setProperty("Copy", "Kop\u00EDrovat");
        dictionary.setProperty("Delete", "Smazat");
        dictionary.setProperty("Save", "Ulo\u017Eit");
        dictionary.setProperty("Reload", "Obnovit");
        dictionary.setProperty("Export", "Exportovat");
        dictionary.setProperty("Filter", "Filtrovat");

        // MDI Frame...
        dictionary.setProperty("file", "Soubor");
        dictionary.setProperty("exit", "Konec");
        dictionary.setProperty("filemnemonic", "F");
        dictionary.setProperty("exitmnemonic", "E");
        dictionary.setProperty("change user", "Zm\u011Bnit u\u017Eivatele");
        dictionary.setProperty("changeusermnemonic", "U");
        dictionary.setProperty("changelanguagemnemonic", "L");
        dictionary.setProperty("help", "N\u00E1pov\u011Bda");
        dictionary.setProperty("about", "O programu");
        dictionary.setProperty("helpmnemonic", "H");
        dictionary.setProperty("aboutmnemonic", "A");
        dictionary.setProperty("are you sure to quit application?", "Opravdu chcete ukon\u010Dit program?");
        dictionary.setProperty("quit application", "Ukon\u010Dit program");
        dictionary.setProperty("forcegcmnemonic", "F");
        dictionary.setProperty("Force GC", "Vynutit GC");
        dictionary.setProperty("Java Heap", "Java halda");
        dictionary.setProperty("used", "pou\u017Eito");
        dictionary.setProperty("allocated", "p\u0159id\u011Bleno");
        dictionary.setProperty("change language", "Zm\u011Bnit jazyk");
        dictionary.setProperty("changemnemonic", "L");
        dictionary.setProperty("cancelmnemonic", "C");
        dictionary.setProperty("cancel", "Zru\u0161it");
        dictionary.setProperty("yes", "Ano");
        dictionary.setProperty("no", "Ne");
        dictionary.setProperty("Functions", "Funkce");
        dictionary.setProperty("Error while executing function", "Chyba p\u0159i v\u00FDkonu funkce");
        dictionary.setProperty("Error", "Chyba");
        dictionary.setProperty("infoPanel", "Informace");
        dictionary.setProperty("imageButton", "O programu");
        dictionary.setProperty("Window", "Okno");
        dictionary.setProperty("windowmnemonic", "W");
        dictionary.setProperty("Close All", "Zav\u0159\u00EDt v\u0161e");
        dictionary.setProperty("closeallmnemonic", "A");
        dictionary.setProperty("closemnemonic", "Z");
        dictionary.setProperty("Press ENTER to find function", "Pro nalezen\u00ED funkce stiskn\u011Bte ENTER");
        dictionary.setProperty("Find Function", "Nal\u00E9zt funkci");
        dictionary.setProperty("Operation in progress...", "Operace prob\u00EDh\u00E1...");
        dictionary.setProperty("close window", "Zav\u0159\u00EDt okno");
        dictionary.setProperty("reduce to icon", "Shodit na li\u0161tu");
        dictionary.setProperty("save changes?", "Ulo\u017Eit zm\u011Bny?");
        dictionary.setProperty("confirm window closing", "Potvrdit zav\u0159en\u00ED okna");
        dictionary.setProperty("change background", "Zm\u011Bnit pozad\u00ED");
        dictionary.setProperty("reset background", "Obnovit pozad\u00ED");

        dictionary.setProperty("switch", "P\u0159epnout");
        dictionary.setProperty("switchmnemonic", "P");
        dictionary.setProperty("window name", "N\u00E1zev okna");
        dictionary.setProperty("opened windows", "Otev\u0159en\u00E1 okna");
        dictionary.setProperty("tile horizontally", "Dla\u017Edicov\u011B vodorovn\u011B");
        dictionary.setProperty("tilehorizontallymnemonic", "V");
        dictionary.setProperty("tile vertically", "Dla\u017Edicov\u011B svisle");
        dictionary.setProperty("tileverticallymnemonic", "S");
        dictionary.setProperty("cascade", "Kask\u00E1dov\u011B");
        dictionary.setProperty("cascademnemonic", "C");
        dictionary.setProperty("minimize", "Minimalizovat");
        dictionary.setProperty("minimizemnemonic", "M");
        dictionary.setProperty("minimize all", "Minimalizovat v\u0161e");
        dictionary.setProperty("minimizeallmnemonic", "A");
        dictionary.setProperty("selected frame","vybran\u00E9 okno");

        // server...
        dictionary.setProperty("Client request not supported", "\u017D\u00E1dost klienta nen\u00ED podporov\u00E1na");
        dictionary.setProperty("User disconnected", "U\u017Eivatel je odpojen");
        dictionary.setProperty("Updating not performed: the record was previously updated.", "Aktualizace neprovedena: z\u00E1znam u\u017E byl d\u0159\u00EDve aktualizov\u00E1n.");

        // wizard...
        dictionary.setProperty("back", "Zp\u011Bt");
        dictionary.setProperty("next", "Dal\u0161\u00ED");
        dictionary.setProperty("finish", "Dokon\u010Dit");

        // image panel...
        dictionary.setProperty("image selection", "V\u00FDb\u011Br obr\u00E1zku");

        // tip of the day panel...
        dictionary.setProperty("show 'tip of the day' after launching", "Zobrazit 'tip dne' po startu");
        dictionary.setProperty("previous tip", "P\u0159edchoz\u00ED tip");
        dictionary.setProperty("next tip", "N\u00E1sleduj\u00EDc\u00ED tip");
        dictionary.setProperty("close", "Zav\u0159\u00EDt");
        dictionary.setProperty("tip of the day", "Tip dne");
        dictionary.setProperty("select tip", "Vybrat tip");
        dictionary.setProperty("tip name", "N\u00E1zev tipu");
        dictionary.setProperty("tips list", "Seznam tip\u016F");

        // progress panel...
        dictionary.setProperty("progress", "Pr\u016Fb\u011Bh");

        // licence agreement...
        dictionary.setProperty("i accept the terms in the licence agreement", "Souhlas\u00EDm s podm\u00EDnkami uveden\u00FDmi v licen\u010Dn\u00ED smlouv\u011B");
        dictionary.setProperty("ok", "Ok");
        dictionary.setProperty("i do not accept the terms in the licence agreement", "Nesouhlas\u00EDm s podm\u00EDnkami uveden\u00FDmi v licen\u010Dn\u00ED smlouv\u011B");

        // property grid control
        dictionary.setProperty("property name", "N\u00E1zev");
        dictionary.setProperty("property value", "Hodnota");

        // grid profile
        dictionary.setProperty("grid profile management", "Spr\u00E1va profil\u016F tabulky");
        dictionary.setProperty("restore default grid profile", "Obnovit v\u00FDchoz\u00ED profil tabulky");
        dictionary.setProperty("create new grid profile", "Vytvo\u0159it nov\u00FD profil tabulky");
        dictionary.setProperty("profile description", "Popis profilu");
        dictionary.setProperty("remove current grid profile", "Odstranit sou\u010Dasn\u00FD profil tabulky");
        dictionary.setProperty("select grid profile", "Vybrat profil tabulky");
        dictionary.setProperty("default profile", "V\u00FDchoz\u00ED profil");

        // search box
        dictionary.setProperty("search", "Hledat");
        dictionary.setProperty("not found", "Nenalezeno");

        // drag...
        dictionary.setProperty("drag", "T\u00E1hnout");

        // pivot table...
        dictionary.setProperty("pivot table settings", "Nastaven\u00ED pivotn\u00ED tabulky");
        dictionary.setProperty("row fields", "Pole \u0159\u00E1dk\u016F");
        dictionary.setProperty("column fields", "Pole sloupc\u016F");
        dictionary.setProperty("data fields", "Pole dat");
        dictionary.setProperty("filtering conditions", "Podm\u00EDnky filtrov\u00E1n\u00ED");
        dictionary.setProperty("field", "Pole");
        dictionary.setProperty("checked", "Za\u0161krtnuto");
        dictionary.setProperty("at least one data field must be selected", "Mus\u00ED b\u00FDt vybr\u00E1no nejm\u00E9n\u011B jedno pole dat.");
        dictionary.setProperty("at least one row field must be selected", "Mus\u00ED b\u00FDt vybr\u00E1no nejm\u00E9n\u011B jedno pole \u0159\u00E1dk\u016F.");
        dictionary.setProperty("at least one column field must be selected", "Mus\u00ED b\u00FDt vybr\u00E1no nejm\u00E9n\u011B jedno pole sloupc\u016F.");
        dictionary.setProperty("expand all", "Rozt\u00E1hnout v\u0161e");
        dictionary.setProperty("collapse all", "Sbalit v\u0161e");

        dictionary.setProperty(Consts.EQ, "rovn\u00E1 se");
        dictionary.setProperty(Consts.GE, "je v\u011Bt\u0161\u00ED nebo rovn\u00E1 se");
        dictionary.setProperty(Consts.GT, "je v\u011Bt\u0161\u00ED ne\u017E");
        dictionary.setProperty(Consts.IS_NOT_NULL, "je vypln\u011Bno");
        dictionary.setProperty(Consts.IS_NULL, "nen\u00ED vypln\u011Bno");
        dictionary.setProperty(Consts.LE, "je men\u0161\u00ED nebo rovn\u00E1 se");
        dictionary.setProperty(Consts.LIKE, "obsahuje");
        dictionary.setProperty(Consts.LT, "je men\u0161\u00ED ne\u017E");
        dictionary.setProperty(Consts.NEQ, "se nerovn\u00E1");
        dictionary.setProperty(Consts.IN, "obsahuje hodnoty");
        dictionary.setProperty(Consts.ASC_SORTED, "vzestupn\u011B");
        dictionary.setProperty(Consts.DESC_SORTED, "sestupn\u011B");
        dictionary.setProperty(Consts.NOT_IN, "neobsahuje hodnoty");

        resources = new Resources(
                dictionary,
                currencySymbol,
                ',',
                ' ',
                Resources.DMY,
                true,
                dateFormatSeparator,
                "HH:mm",
                "CS",
                showResourceNotFoundWarning);
    }

    /**
     * Constructor.
     * @param additionalDictionary additional descriptions
     * @param showResourceNotFoundWarning warn when no resource key not found
     */
    public CzechOnlyResourceFactory(Properties additionalDictionary, boolean showResourceNotFoundWarning) {
        this("K\u010D", additionalDictionary, showResourceNotFoundWarning);
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
            throw new UnsupportedOperationException("Identifik\u00E1tor jazyka nen\u00ED podporov\u00E1n.");
        }
    }

    /**
     * @param langId language id identifier
     * @return internationalization settings, according with the language specified
     */
    public final Resources getResources(String langId) throws UnsupportedOperationException {
        if (!resources.getLanguageId().equals(langId)) {
            throw new UnsupportedOperationException("Identifik\u00E1tor jazyka nen\u00ED podporov\u00E1n.");
        }
        return resources;
    }
}
