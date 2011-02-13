package org.openswing.swing.internationalization.java;

import java.util.*;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.internationalization.java.ResourcesFactory;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieve the collection of all internationalization properties:
 * translations, data/numeric/currency formats.
 * No translation is performed, date/numeric/currency formats are based on Polish formats.</p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
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
 *       The author of polish translate may be contacted at:
 *                     ujacek@tlen.pl</p>
 *
 * @author Mauro Carniel/Jacek Uznanski
 * @version 1.0
 */
public class PolishOnlyResourceFactory extends ResourcesFactory {

    /** internationalization settings */
    private Resources resources = null;


    /**
     * Constructor.
     * @param currencySymbol currency symbol
     * @param additionalDictionary additional descriptions
     * @param showResourceNotFoundWarning warn when no resource key not found
     */
    public PolishOnlyResourceFactory(String currencySymbol, Properties additionalDictionary, boolean showResourceNotFoundWarning) {
        Properties dictionary = new Properties();

        dictionary.putAll(additionalDictionary);

        // grid...
        dictionary.setProperty("of","z");
        dictionary.setProperty("page","Strona");
        dictionary.setProperty("Remove Filter", "Usu\u0144 warunki wybierania");
        dictionary.setProperty("This column is not sorteable", "Ta kolumna nie podlega sortowaniu");
        dictionary.setProperty("Sorting not allowed", "Sortowanie jest niemo\u017cliwe");
        dictionary.setProperty("Maximum number of sorted columns", "Wykorzystano maksymaln\u0105 liczb\u0119 sortowanych kolumn");
        dictionary.setProperty("Sorting not applicable", "Ograniczenie sortowania");
        dictionary.setProperty("Selected Rows", "Wybrane pozycje");
        dictionary.setProperty("Selected Row", "Wybrana pozycja");
        dictionary.setProperty("Cancel changes and reload data?", "Czy porzuci\u0107 zmiany i prze\u0142adowa\u0107 dane?");
        dictionary.setProperty("Attention", "Uwaga");
        dictionary.setProperty("Loading data...", "Trwa pobieranie danych...");
        dictionary.setProperty("Error while loading data", "Wyst\u0105pi\u0142 b\u0142\u0105d podczas pobierania danych");
        dictionary.setProperty("Loading Data Error", "B\u0142\u0105d pobierania danych");
        dictionary.setProperty("Delete Rows?", "Czy usun\u0105\u0107 wskazane pozycje?");
        dictionary.setProperty("Delete Confirmation", "Potwierdzenie usuni\u0119cia");
        dictionary.setProperty("Error while deleting rows.", "Wyst\u0105pi\u0142 b\u0142\u0105d podczas usuwania danych.");
        dictionary.setProperty("Deleting Error", "B\u0142\u0105d kasowania danych");
        dictionary.setProperty("Error while saving", "Wyst\u0105pi\u0142 b\u0142\u0105d podczas zapisywania danych.");
        dictionary.setProperty("Saving Error", "B\u0142\u0105d zapisywania danych");
        dictionary.setProperty("A mandatory column is empty.", "Brak warto\u015bci dla wymaganej kolumny.");
        dictionary.setProperty("Value not valid", "Nieprawid\u0142owa warto\u015b\u0107");
        dictionary.setProperty("sorting conditions", "Warunki sortowania");
        dictionary.setProperty("filtering conditions", "Warunki wybierania");
        dictionary.setProperty("filtering and sorting settings", "Warunki wybierania i sortowania");
        dictionary.setProperty("Filtering/Sorting data (CTRL+F)", "Warunki wybierania i sortowania (CTRL+F)");
        dictionary.setProperty("upload file", "Wys\u0142anie pliku");
        dictionary.setProperty("download file", "Pobranie pliku");

        // export...
        dictionary.setProperty("grid export", "Eksport danych z tabeli");
        dictionary.setProperty("export", "Eksportuj");
        dictionary.setProperty("exportmnemonic", "X");
        dictionary.setProperty("column", "Kolumna");
        dictionary.setProperty("sel.", "Wyb.");
        dictionary.setProperty("you must select at least one column", "Musisz wybra\u0107 co najmniej jedn\u0105 kolumn\u0119");
        dictionary.setProperty("columns to export", "Kolumny do wyeksportowania");
        dictionary.setProperty("export type", "Format eksportu");

        // import...
        dictionary.setProperty("grid import", "Import danych do tabeli");
        dictionary.setProperty("file to import", "Plik do zaimportowania");
        dictionary.setProperty("import", "Importuj");
        dictionary.setProperty("importmnemonic", "M");
        dictionary.setProperty("columns to import", "Kolumny do zaimportowania");
        dictionary.setProperty("import type", "Format importu");
        dictionary.setProperty("error while importing data", "Wyst\u0105pi\u0142 b\u0142\u0105d podczas importowania danych");
        dictionary.setProperty("import completed", "Importowanie zako\u0144czone");

        // quick filter...
        dictionary.setProperty("To value", "Do warto\u015bci");
        dictionary.setProperty("Filter by", "Wybierz dla");
        dictionary.setProperty("From value", "Od warto\u015bci");
        dictionary.setProperty("equals", "r\u00f3wne");
        dictionary.setProperty("contains", "zawiera");
        dictionary.setProperty("starts with", "zaczyna si\u0119");
        dictionary.setProperty("ends with", "ko\u0144czy si\u0119");

        // select/deselect all
        dictionary.setProperty("select all", "Zaznacz wszystko");
        dictionary.setProperty("deselect all", "Odznacz wszystko");

        // copy/cut/paste
        dictionary.setProperty("copy", "Kopiuj");
        dictionary.setProperty("copymnemonic", "C");
        dictionary.setProperty("cut", "Wytnij");
        dictionary.setProperty("cutmnemonic", "U");
        dictionary.setProperty("paste", "Wklej");
        dictionary.setProperty("pastemnemonic", "P");

        // lookup...
        dictionary.setProperty("Code is not correct.", "Podany symbol jest nieprawid\u0142owy.");
        dictionary.setProperty("Code Validation", "Sprawdzenie symbolu");
        dictionary.setProperty("Code Selection", "Wyb\u00f3r symbolu");

        // form...
        dictionary.setProperty("Confirm deliting data?", "Czy potwierdzasz usuni\u0119cie danych?");
        dictionary.setProperty("Error while saving: incorrect data.", "Wyst\u0105pi\u0142 b\u0142\u0105d podczas zapisywania danych: nieprawid\u0142owe dane.");
        dictionary.setProperty("Error while validating data:","Brak lub b\u0142\u0119dna warto\u015B\u0107 dla:");
        dictionary.setProperty("Validation Error","B\u0142\u0105d kontroli danych");
        dictionary.setProperty("Error on deleting:", "B\u0142\u0105d podczas usuwania:");
        dictionary.setProperty("Error on Loading", "B\u0142\u0105d podczas \u0142adowania");
        dictionary.setProperty("Error while loading data:", "Wyst\u0105pi\u0142 b\u0142\u0105d podczas \u0142adowania danych:");
        dictionary.setProperty("Error on setting value to the input control having the attribute name", "B\u0142\u0105d nadania warto\u015bci elementowi edycji dla atrybutu");


        // toolbar buttons...
        dictionary.setProperty("Delete record (CTRL+D)", "Usu\u0144 (CTRL+D)");
        dictionary.setProperty("Edit record (CTRL+E)", "Zmie\u0144 (CTRL+E)");
        dictionary.setProperty("New record (CTRL+I)", "Dopisz (CTRL+I)");
        dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)", "Od\u015bwie\u017c/Zrezygnuj (CTRL+Z)");
        dictionary.setProperty("Save record (CTRL+S)", "Zapisz (CTRL+S)");
        dictionary.setProperty("Copy record (CTRL+C)", "Kopiuj (CTRL+C)");
        dictionary.setProperty("Export record (CTRL+X)", "Eksportuj (CTRL+X)");
        dictionary.setProperty("Import records (CTRL+M)", "Importuj (CTRL+M)");
        dictionary.setProperty("Load the first block of records", "Pocz\u0105tek danych");
        dictionary.setProperty("Select the previous row in grid", "Poprzednia pozycja");
        dictionary.setProperty("Select the next row in grid", "Nast\u0119pna pozycja");
        dictionary.setProperty("Load the previous block of records", "Poprzedni blok danych");
        dictionary.setProperty("Load the next block of records", "Nast\u0119pny blok danych");
        dictionary.setProperty("Load the last block of records", "Koniec danych");

        dictionary.setProperty("Insert", "Wstaw");
        dictionary.setProperty("Edit", "Zmie\u0144");
        dictionary.setProperty("Copy", "Kopiuj");
        dictionary.setProperty("Delete", "Usu\u0144");
        dictionary.setProperty("Save", "Zapisz");
        dictionary.setProperty("Reload", "Od\u015bwie\u017c");
        dictionary.setProperty("Export", "Eksportuj");
        dictionary.setProperty("Filter", "Filtruj");

        // MDI Frame...
        dictionary.setProperty("file", "Program");
        dictionary.setProperty("exit", "Koniec");
        dictionary.setProperty("filemnemonic", "F");
        dictionary.setProperty("exitmnemonic", "E");
        dictionary.setProperty("change user", "Zmie\u0144 u\u017cytkownika");
        dictionary.setProperty("changeusermnemonic", "U");
        dictionary.setProperty("changelanguagemnemonic", "L");
        dictionary.setProperty("help", "Pomoc");
        dictionary.setProperty("about", "O programie");
        dictionary.setProperty("helpmnemonic", "H");
        dictionary.setProperty("aboutmnemonic", "A");
        dictionary.setProperty("are you sure to quit application?", "Czy zako\u0144czy\u0107 dzia\u0142anie aplikacji?");
        dictionary.setProperty("quit application", "Zamknij aplikacj\u0119");
        dictionary.setProperty("forcegcmnemonic", "F");
        dictionary.setProperty("Force GC", "Uruchom GC");
        dictionary.setProperty("Java Heap", "Java Heap");
        dictionary.setProperty("used", "u\u017cyte");
        dictionary.setProperty("allocated", "przydzielone");
        dictionary.setProperty("change language", "Zmie\u0144 j\u0119zyk");
        dictionary.setProperty("changemnemonic", "L");
        dictionary.setProperty("cancelmnemonic", "C");
        dictionary.setProperty("cancel", "Zrezygnuj");
        dictionary.setProperty("yes", "Tak");
        dictionary.setProperty("no", "Nie");
        dictionary.setProperty("Functions", "Funkcje");
        dictionary.setProperty("Error while executing function", "B\u0142\u0105d podczas wywo\u0142ania funkcji");
        dictionary.setProperty("Error", "B\u0142\u0105d");
        dictionary.setProperty("infoPanel", "Informacje");
        dictionary.setProperty("imageButton", "About");
        dictionary.setProperty("Window", "Okna");
        dictionary.setProperty("windowmnemonic", "W");
        dictionary.setProperty("Close All", "Zamknij wszystkie");
        dictionary.setProperty("closeallmnemonic", "A");
        dictionary.setProperty("closemnemonic","Z");
        dictionary.setProperty("Press ENTER to find function", "Naci\u015bnij ENTER by znale\u017a\u0107 funkcj\u0119");
        dictionary.setProperty("Find Function", "Znajd\u017a Funkcj\u0119");
        dictionary.setProperty("Operation in progress...", "Operacja w toku...");
        dictionary.setProperty("close window", "Zamknij okno");
        dictionary.setProperty("reduce to icon", "Zmniejsz do ikony");
        dictionary.setProperty("are you sure to close this window?", "Czy jeste\u015b pewien \u017ce chcesz zamkn\u0105c to okno?\nNiezapami\u0119tane zmiany zostan\u0105 utracone.");
        dictionary.setProperty("confirm window closing", "Potwierd\u017a zamkni\u0119cie okna");
        dictionary.setProperty("change background","Zmie\u0144 t\u0142o");
        dictionary.setProperty("reset background","Przywr\u00f3\u0107 t\u0142o");

        dictionary.setProperty("switch", "Prze\u0142\u0105cz");
        dictionary.setProperty("switchmnemonic", "S");
        dictionary.setProperty("window name", "Nazwa okna");
        dictionary.setProperty("opened windows", "Otwarte okna");
        dictionary.setProperty("tile horizontally", "Uk\u0142ad poziomy");
        dictionary.setProperty("tilehorizontallymnemonic", "H");
        dictionary.setProperty("tile vertically", "Uk\u0142ad pionowy");
        dictionary.setProperty("tileverticallymnemonic", "V");
        dictionary.setProperty("cascade", "Kaskada");
        dictionary.setProperty("cascademnemonic", "C");
        dictionary.setProperty("minimize", "Minimalizuj");
        dictionary.setProperty("minimizemnemonic", "M");
        dictionary.setProperty("minimize all", "Minimalizuj wszystkie");
        dictionary.setProperty("minimizeallmnemonic", "A");
        dictionary.setProperty("selected frame","wybrane okno");

        // server...
        dictionary.setProperty("Client request not supported", "\u017b\u0105dania do klienta nie s\u0105 obs\u0142ugiwane");
        dictionary.setProperty("User disconnected", "U\u017cytkownik jest od\u0142\u0105czony");
        dictionary.setProperty("Updating not performed: the record was previously updated.", "Aktualizacja si\u0119 nie powiod\u0142a: rekord by\u0142 juz uprzednio zaktualizowany.");

        // wizard...
        dictionary.setProperty("back", "Wstecz");
        dictionary.setProperty("next", "Dalej");
        dictionary.setProperty("finish", "Koniec");

        // image panel...
        dictionary.setProperty("image selection", "Wyb\u00f3r obrazu");

        // tip of the day panel...
        dictionary.setProperty("show 'tip of the day' after launching", "Pokazuj 'porad\u0119 dnia' po uruchomieniu aplikacji");
        dictionary.setProperty("previous tip", "Poprzednia");
        dictionary.setProperty("next tip", "Nast\u0119pna");
        dictionary.setProperty("close", "Zamknij");
        dictionary.setProperty("tip of the day", "Porada dnia");
        dictionary.setProperty("select tip", "Wybierz");
        dictionary.setProperty("tip name", "Tytu\u0142");
        dictionary.setProperty("tips list", "Lista porad");

        // progress panel...
        dictionary.setProperty("progress", "Post\u0119p");

        // licence agreement...
        dictionary.setProperty("i accept the terms in the licence agreement", "Akceptuj\u0119 warunki zawarte w umowie licencyjnej");
        dictionary.setProperty("ok", "Zatwierd\u017a");
        dictionary.setProperty("i do not accept the terms in the licence agreement", "Nie akceptuj\u0119 warunk\u00f3w umowy licencyjnej");

        // property grid control
        dictionary.setProperty("property name", "Nazwa");
        dictionary.setProperty("property value", "Warto\u015b\u0107");

        // grid profile
        dictionary.setProperty("grid profile management", "Zarz\u0105dzanie profilami uk\u0142adu tabeli");
        dictionary.setProperty("restore default grid profile", "Odtw\u00f3rz domy\u015blny profil uk\u0142adu tabeli");
        dictionary.setProperty("create new grid profile", "Utw\u00f3rz nowy profil uk\u0142adu tabeli");
        dictionary.setProperty("profile description", "Opis profilu");
        dictionary.setProperty("remove current grid profile", "Usu\u0144 bie\u017C\u0105cy profil uk\u0142adu tabeli");
        dictionary.setProperty("select grid profile", "Wybierz profil uk\u0142adu tabeli");
        dictionary.setProperty("default profile", "Profil domy\u015blny");

        // search box
        dictionary.setProperty("search", "Szukaj");
        dictionary.setProperty("not found", "Nie znaleziono");

        // drag...
        dictionary.setProperty("drag", "Ci\u0105gnij");

        dictionary.setProperty("Caps lock pressed","Caps Lock jest w\u0142\u0105czony");

        // pivot table...
        dictionary.setProperty("pivot table settings","Ustawienia dla arkusza");
        dictionary.setProperty("row fields","Wiersze");
        dictionary.setProperty("column fields","Kolumny");
        dictionary.setProperty("data fields","Dane");
        dictionary.setProperty("filtering conditions", "Warunki wybierania");
        dictionary.setProperty("field","Kom\u00f3rka");
        dictionary.setProperty("checked","Wybierz");
        dictionary.setProperty("at least one data field must be selected","Musiby\u0107 wybrana co najmniej jedna kom\u00f3rka danych.");
        dictionary.setProperty("at least one row field must be selected","Musiby\u0107 wybrany co najmniej jeden wiersz.");
        dictionary.setProperty("at least one column field must be selected","Musiby\u0107 wybrana co najmniej jedna kolumna.");
        dictionary.setProperty("expand all","Rozwi\u0144 wszystko");
        dictionary.setProperty("collapse all","Zwi\u0144 wszystko");

        dictionary.setProperty(Consts.EQ,"r\u00f3wne");
        dictionary.setProperty(Consts.GE,"wi\u0119ksze lub r\u00f3wne");
        dictionary.setProperty(Consts.GT,"wi\u0119ksze ni\u017c");
        dictionary.setProperty(Consts.IS_NOT_NULL,"jest niepuste");
        dictionary.setProperty(Consts.IS_NULL,"jest puste");
        dictionary.setProperty(Consts.LE,"mniejsze lub r\u00f3wne");
        dictionary.setProperty(Consts.LIKE,"podobne do");
        dictionary.setProperty(Consts.LT,"mniejsze ni\u017c");
        dictionary.setProperty(Consts.NEQ,"r\u00f3\u017cne od");
        dictionary.setProperty(Consts.IN,"spo\u015br\u00f3d");
        dictionary.setProperty(Consts.ASC_SORTED,"rosn\u0105co");
        dictionary.setProperty(Consts.DESC_SORTED,"malej\u0105co");
        dictionary.setProperty(Consts.NOT_IN,"spoza");

        resources = new Resources(
                dictionary,
                currencySymbol,
                ',',
                '.',
                Resources.YMD,
                true,
                '-',
                "HH:mm",
                "PL",
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
            throw new UnsupportedOperationException("Language identifier not supported.");
        }
    }


    /**
     * @param langId language id identifier
     * @return internationalization settings, according with the language specified
     */
    public final Resources getResources(String langId) throws UnsupportedOperationException {
        if (!resources.getLanguageId().equals(langId)) {
            throw new UnsupportedOperationException("Language identifier not supported.");
        }
        return resources;
    }
}
