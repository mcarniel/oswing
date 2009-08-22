package org.openswing.swing.internationalization.java;

import java.util.*;

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
 * @author Mauro Carniel/Zoltán Zidarics/Attila Szomor
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
        dictionary.setProperty("Remove Filter", "Sz\u0171r\u0151feltétel törlése");
        dictionary.setProperty("This column is not sorteable", "Ezen oszlop alapján nem rendezhet\u0151");
        dictionary.setProperty("Sorting not allowed", "A rendezés nem engedélyezett");
        dictionary.setProperty("Maximum number of sorted columns", "Maximálisan rendezhet\u0151 oszlopok száma");
        dictionary.setProperty("Sorting not applicable", "Rendezés nem hajtható vécgre");
        dictionary.setProperty("Selected Row", "Kijelölt sor");
        dictionary.setProperty("Selected Rows", "Kijelölt sorok");
        dictionary.setProperty("Cancel changes and reload data?", "Eldobja a változtásokat és újratölti az adatokat?");
        dictionary.setProperty("Attention", "Figyelmeztetés");
        dictionary.setProperty("Loading data...", "Adatok betöltése...");
        dictionary.setProperty("Error while loading data", "Hiba betöltés közben");
        dictionary.setProperty("Loading Data Error", "Adathiba");
        dictionary.setProperty("Delete Rows?", "Sor törlése?");
        dictionary.setProperty("Delete Confirmation", "Törlés meger\u0151sítés");
        dictionary.setProperty("Error while deleting rows.", "Hiba sor törlése közben.");
        dictionary.setProperty("Deleting Error", "Törlés hiba");
        dictionary.setProperty("Error while saving", "Hiba mentés közben");
        dictionary.setProperty("Saving Error", "Mentés hiba");
        dictionary.setProperty("A mandatory column is empty.", "A kötelez\u0151en kitöltend\u0151 oszlop üres.");
        dictionary.setProperty("Value not valid", "Hibás érték");
        dictionary.setProperty("sorting conditions", "Rendezési feltételek");
        dictionary.setProperty("filtering conditions", "Sz\u0171rési feltételek");
        dictionary.setProperty("filtering and sorting settings", "Sz\u0171rési és rendezési beállítások");
        dictionary.setProperty("Filtering/Sorting data (CTRL+F)", "Adat sz\u0171rés/rendezés (CTRL+F)");
        dictionary.setProperty("upload file", "Fájl feltöltése");
        dictionary.setProperty("download file", "Fájl letöltése");

        // export...
        dictionary.setProperty("grid export", "Tábla exportálás");
        dictionary.setProperty("export", "Exportálás");
        dictionary.setProperty("exportmnemonic", "X");
        dictionary.setProperty("column", "Oszlop");
        dictionary.setProperty("sel.", "Jelöl");
        dictionary.setProperty("you must select at least one column", "Legalább egy oszlopot ki kell jelölnie");
        dictionary.setProperty("columns to export", "Exportálandó oszlopok");
        dictionary.setProperty("export type", "Export formátum");

        // import...
        dictionary.setProperty("grid import", "Tábla Importálás");
        dictionary.setProperty("file to import", "Import file");
        dictionary.setProperty("import", "Import");
        dictionary.setProperty("importmnemonic", "M");
        dictionary.setProperty("columns to import", "Importálandó oszlopok");
        dictionary.setProperty("import type", "Import formátum");
        dictionary.setProperty("error while importing data", "Hiba importálás közben");
        dictionary.setProperty("import completed", "Importálás kész");

        // quick filter...
        dictionary.setProperty("To value", "Tartalomra");
        dictionary.setProperty("Filter by", "Sz\u0171rés alapja");
        dictionary.setProperty("From value", "Értékt\u0151l");
        dictionary.setProperty("equals", "egyenl\u0151");
        dictionary.setProperty("contains", "tartalmazza");
        dictionary.setProperty("starts with", "kezd\u0151dik");
        dictionary.setProperty("ends with", "végz\u0151dik");

        // select/deselect all
        dictionary.setProperty("select all", "Az összes kiejlölése");
        dictionary.setProperty("deselect all", "Kijelölések megszüntetése");

        // copy/cut/paste
        dictionary.setProperty("copy", "Másol");
        dictionary.setProperty("copymnemonic", "C");
        dictionary.setProperty("cut", "Kivág");
        dictionary.setProperty("cutmnemonic", "X");
        dictionary.setProperty("paste", "Beilleszt");
        dictionary.setProperty("pastemnemonic", "V");

        // lookup...
        dictionary.setProperty("Code is not correct.", "A kód nem korrekt");
        dictionary.setProperty("Code Validation", "Kód validálás");
        dictionary.setProperty("Code Selection", "Kód választás");

        // form...
        dictionary.setProperty("Confirm deliting data?", "Biztos törölni akarja?");
        dictionary.setProperty("Error while saving: incorrect data.", "Hiba mentés közben: inkorrekt adat.");
        dictionary.setProperty("Error on deleting:", "Hiba törlés közben:");
        dictionary.setProperty("Error on Loading", "Hiba betöltés közben");
        dictionary.setProperty("Error while loading data:", "Hiba betöltés közben:");
        dictionary.setProperty("Error on setting value to the input control having the attribute name", "Hiba beállítás közben az attributumnak nincs értéke");

        // toolbar buttons...
        dictionary.setProperty("Delete record (CTRL+D)", "Rekord törlés (CTRL+D)");
        dictionary.setProperty("Edit record (CTRL+E)", "Rekord szerkesztése (CTRL+E)");
        dictionary.setProperty("New record (CTRL+I)", "Új rekord (CTRL+I)");
        dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)", "Az aktuális operáció törlése/rekord újratöltése (CTRL+Z)");
        dictionary.setProperty("Save record (CTRL+S)", "Rekord mentése (CTRL+S)");
        dictionary.setProperty("Copy record (CTRL+C)", "Rekord másolása (CTRL+C)");
        dictionary.setProperty("Export record (CTRL+X)", "Rekord exportálása (CTRL+X)");
        dictionary.setProperty("Import records (CTRL+M)", "Rekords importálása (CTRL+M)");
        dictionary.setProperty("Load the first block of records", "Az els\u0151 blokk betöltése");
        dictionary.setProperty("Select the previous row in grid", "A tábla el\u0151z\u0151 sorának kiválasztása");
        dictionary.setProperty("Select the next row in grid", "A tábla következ\u0151 sorának kiválasztása");
        dictionary.setProperty("Load the previous block of records", "Az el\u0151z\u0151 blokk betöltése");
        dictionary.setProperty("Load the next block of records", "A következ\u0151 blokk betöltése");
        dictionary.setProperty("Load the last block of records", "Az utolsó blokk betöltése");

        dictionary.setProperty("Insert", "Beszúrás");
        dictionary.setProperty("Edit", "Szerkesztés");
        dictionary.setProperty("Copy", "Másolás");
        dictionary.setProperty("Delete", "Törlés");
        dictionary.setProperty("Save", "Mentés");
        dictionary.setProperty("Reload", "Újratöltés");
        dictionary.setProperty("Export", "Export");
        dictionary.setProperty("Filter", "Sz\u0171rés");

        // MDI Frame...
        dictionary.setProperty("file", "Fájl");
        dictionary.setProperty("exit", "Kilépés");
        dictionary.setProperty("filemnemonic", "F");
        dictionary.setProperty("exitmnemonic", "K");
        dictionary.setProperty("change user", "Felhasználó váltás");
        dictionary.setProperty("changeusermnemonic", "V");
        dictionary.setProperty("changelanguagemnemonic", "L");
        dictionary.setProperty("help", "Súgó");
        dictionary.setProperty("about", "Névjegy");
        dictionary.setProperty("helpmnemonic", "S");
        dictionary.setProperty("aboutmnemonic", "N");
        dictionary.setProperty("are you sure to quit application?", "Biztosan kilép?");
        dictionary.setProperty("quit application", "Kilépés");
        dictionary.setProperty("forcegcmnemonic", "F");
        dictionary.setProperty("Force GC", "Tisztogatás");
        dictionary.setProperty("Java Heap", "Java Heap");
        dictionary.setProperty("used", "használt");
        dictionary.setProperty("allocated", "allokálva");
        dictionary.setProperty("change language", "Nyelv váltás");
        dictionary.setProperty("changemnemonic", "N");
        dictionary.setProperty("cancelmnemonic", "C");
        dictionary.setProperty("cancel", "Eldob");
        dictionary.setProperty("yes", "Igen");
        dictionary.setProperty("no", "Nem");
        dictionary.setProperty("Functions", "Funkciók");
        dictionary.setProperty("Error while executing function", "Hiba a funkció végrehajtása közben");
        dictionary.setProperty("Error", "Hiba");
        dictionary.setProperty("infoPanel", "Info");
        dictionary.setProperty("imageButton", "Névjegy");
        dictionary.setProperty("Window", "Ablak");
        dictionary.setProperty("windowmnemonic", "W");
        dictionary.setProperty("Close All", "Bezár mindent");
        dictionary.setProperty("closeallmnemonic", "A");
        dictionary.setProperty("Press ENTER to find function", "ENTER a funkció kereséshez");
        dictionary.setProperty("Find Function", "Funkció keresés");
        dictionary.setProperty("Operation in progress...", "Operáció végrehajtás alatt...");
        dictionary.setProperty("close window", "Ablak bezárás");
        dictionary.setProperty("reduce to icon", "Ikonméret");
        dictionary.setProperty("save changes?", "Változások elmentése?");
        dictionary.setProperty("confirm window closing", "Ablak bezárás meger\u0151sítése");
        dictionary.setProperty("change background", "Háttér csere");
        dictionary.setProperty("reset background", "Háttér frissítés");

        dictionary.setProperty("switch", "Váltás");
        dictionary.setProperty("switchmnemonic", "S");
        dictionary.setProperty("window name", "Ablak neve");
        dictionary.setProperty("opened windows", "Nyitott ablakok");
        dictionary.setProperty("tile horizontally", "Vízszintes elrendezés");
        dictionary.setProperty("tilehorizontallymnemonic", "H");
        dictionary.setProperty("tile vertically", "Függ\u0151leges elrendezés");
        dictionary.setProperty("tileverticallymnemonic", "V");
        dictionary.setProperty("cascade", "Lezúduló elrendezés");
        dictionary.setProperty("cascademnemonic", "C");
        dictionary.setProperty("minimize", "Kis méret");
        dictionary.setProperty("minimizemnemonic", "M");
        dictionary.setProperty("minimize all", "Mind kis méretre");
        dictionary.setProperty("minimizeallmnemonic", "A");

        // server...
        dictionary.setProperty("Client request not supported", "A kérés kiszolgálása nem megvalósított");
        dictionary.setProperty("User disconnected", "Felhasználó kiécpett");
        dictionary.setProperty("Updating not performed: the record was previously updated.", "A módosítás nem sikerült: a rekordot el\u0151z\u0151leg módosították.");

        // wizard...
        dictionary.setProperty("back", "Vissza");
        dictionary.setProperty("next", "Következ\u0151");
        dictionary.setProperty("finish", "Vége");

        // image panel...
        dictionary.setProperty("image selection", "Kép választás");

        // tip of the day panel...
        dictionary.setProperty("show 'tip of the day' after launching", "Programindításkor napi tippek");
        dictionary.setProperty("previous tip", "El\u0151z\u0151 tipp");
        dictionary.setProperty("next tip", "Következ\u0151 tipp");
        dictionary.setProperty("close", "Bezár");
        dictionary.setProperty("tip of the day", "Napi tippek");
        dictionary.setProperty("select tip", "Tipp kiválasztás");
        dictionary.setProperty("tip name", "Tipp neve");
        dictionary.setProperty("tips list", "Tippek listája");

        // progress panel...
        dictionary.setProperty("progress", "Folyamatban");

        // licence agreement...
        dictionary.setProperty("i accept the terms in the licence agreement", "Elfogadom a licensz-szerz\u0151désben foglaltakat");
        dictionary.setProperty("ok", "Rendben");
        dictionary.setProperty("i do not accept the terms in the licence agreement", "Nem fogadom el a licensz-szerz\u0151désben foglaltakat");

        // property grid control
        dictionary.setProperty("property name", "Név");
        dictionary.setProperty("property value", "Érték");

        // grid profile
        dictionary.setProperty("grid profile management", "Tábla profil management");
        dictionary.setProperty("restore default grid profile", "Az alapértelmezett táblaprofil visszatöltése");
        dictionary.setProperty("create new grid profile", "Új táblaprofil");
        dictionary.setProperty("profile description", "Profil leírás");
        dictionary.setProperty("remove current grid profile", "Táblaprofil törlése");
        dictionary.setProperty("select grid profile", "Táblaprofil választás");
        dictionary.setProperty("default profile", "Alapértelmezett profil");

        // search box
        dictionary.setProperty("search", "Keresés");
        dictionary.setProperty("not found", "Nem található");

        // drag...
        dictionary.setProperty("drag", "Drag");

        // pivot table...
        dictionary.setProperty("pivot table settings", "Pivot tábla beállítások");
        dictionary.setProperty("row fields", "Sor mez\u0151k");
        dictionary.setProperty("column fields", "Oszlop mez\u0151k");
        dictionary.setProperty("data fields", "Adat mez\u0151k");
        dictionary.setProperty("filtering conditions", "Sz\u0171rési feltételek");
        dictionary.setProperty("field", "Mez\u0151");
        dictionary.setProperty("checked", "Kijelölt");
        dictionary.setProperty("at least one data field must be selected", "Minimum egy adat mez\u0151t kötelez\u0151 választani.");
        dictionary.setProperty("at least one row field must be selected", "Minimum egy sor mez\u0151t kötelez\u0151 választani.");
        dictionary.setProperty("at least one column field must be selected", "Minimum egy oszlop mez\u0151t kötelez\u0151 választani.");
        dictionary.setProperty("expand all", "Felfedi mind");
        dictionary.setProperty("collapse all", "Elrejti mind");

        // LoginDialog
        dictionary.setProperty("Logon", "Bejelentkezés");
        dictionary.setProperty("Login", "Belépés");
        dictionary.setProperty("L", "B");
        dictionary.setProperty("Exit", "Kilépés");
        dictionary.setProperty("E", "K");
        dictionary.setProperty("store account", "Elmentsük a felhasználót");
        dictionary.setProperty("Username", "Felhasználó");
        dictionary.setProperty("Password", "Jelszó");

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
            throw new UnsupportedOperationException("Nem támogatott nyelv.");
        }
    }

    /**
     * @param langId language id identifier
     * @return internationalization settings, according with the language specified
     */
    public final Resources getResources(String langId) throws UnsupportedOperationException {
        if (!resources.getLanguageId().equals(langId)) {
            throw new UnsupportedOperationException("Nem támogatott nyelv.");
        }
        return resources;
    }
}
