package org.openswing.swing.internationalization.java;

import java.util.*;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.internationalization.java.ResourcesFactory;


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
 * @author Mauro Carniel/Jacek Uznaäski
 * @version 1.0, based on version 1.7.5
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
        dictionary.setProperty("Remove Filter", "Usuñ warunki wybierania");
        dictionary.setProperty("This column is not sorteable", "Ta kolumna nie podlega sortowaniu");
        dictionary.setProperty("Sorting not allowed", "Sortowanie jest niemo¿liwe");
        dictionary.setProperty("Maximum number of sorted columns", "Wykorzystano maksymaln¹ liczbê sortowanych kolumn");
        dictionary.setProperty("Sorting not applicable", "Ograniczenie sortowania");
        dictionary.setProperty("Selected Row", "Wybrana pozycja");
        dictionary.setProperty("Selected Rows", "Wybrana pozycje");
        dictionary.setProperty("Cancel changes and reload data?", "Czy porzuciæ zmiany i prze³adowaæ dane?");
        dictionary.setProperty("Attention", "Uwaga");
        dictionary.setProperty("Loading data...", "Trwa pobieranie danych...");
        dictionary.setProperty("Error while loading data", "Wyst¹pi³ b³¹d podczas pobierania danych");
        dictionary.setProperty("Loading Data Error", "B³¹d pobierania danych");
        dictionary.setProperty("Delete Rows?", "Czy usun¹æ wskazane pozycje?");
        dictionary.setProperty("Delete Confirmation", "Potwierdzenie usuniêcia");
        dictionary.setProperty("Error while deleting rows.", "Wyst¹pi³ b³¹d podczas usuwania danych.");
        dictionary.setProperty("Deleting Error", "B³¹d kasowania danych");
        dictionary.setProperty("Error while saving", "Wyst¹pi³ b³¹d podczas zapisywania danych.");
        dictionary.setProperty("Saving Error", "B³¹d zapisywania danych");
        dictionary.setProperty("A mandatory column is empty.", "Brak wartoœci dla wymaganej kolumny.");
        dictionary.setProperty("Value not valid", "Nieprawid³owa wartoœæ");
        dictionary.setProperty("sorting conditions", "Warunki sortowania");
        dictionary.setProperty("filtering conditions", "Warunki wybierania");
        dictionary.setProperty("filtering and sorting settings", "Warunki wybierania i sortowania"); //FIXME: ? "Ustawienia wybieranai i sortowania"
        dictionary.setProperty("Filtering/Sorting data (CTRL+F)", "Warunki wybierania i sortowania (CTRL+F)");
        dictionary.setProperty("upload file", "Wys³anie pliku");
        dictionary.setProperty("download file", "Pobranie pliku");

        // export...
        dictionary.setProperty("grid export", "Eksport danych z tabeli");
        dictionary.setProperty("export", "Eksportuj");
        dictionary.setProperty("exportmnemonic", "X");
        dictionary.setProperty("column", "Kolumna");
        dictionary.setProperty("sel.", "Wyb.");
        dictionary.setProperty("you must select at least one column", "Musisz wybraæ co najmniej jedn¹ kolumnê");
        dictionary.setProperty("columns to export", "Kolumny do wyeksportowania");
        dictionary.setProperty("export type", "Format eksportu");

        // import...
        dictionary.setProperty("grid import", "Import danych do tabeli");
        dictionary.setProperty("file to import", "Plik do zaimportowania");
        dictionary.setProperty("import", "Importuj");
        dictionary.setProperty("importmnemonic", "M");
        dictionary.setProperty("columns to import", "Kolumny do zaimportowania");
        dictionary.setProperty("import type", "Format importu");
        dictionary.setProperty("error while importing data", "Wyst¹pi³ b³¹d podczas importowania danych");
        dictionary.setProperty("import completed", "Importowanie zakoñczone");

        // quick filter...
        dictionary.setProperty("To value", "Do wartoœci");
        dictionary.setProperty("Filter by", "Wybierz dla");
        dictionary.setProperty("From value", "Od wartoœci");
        dictionary.setProperty("equals", "równe");
        dictionary.setProperty("contains", "zawiera");
        dictionary.setProperty("starts with", "zaczyna siê");
        dictionary.setProperty("ends with", "koñczy siê");

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
        dictionary.setProperty("Code is not correct.", "Podany symbol jest nieprawid³owy.");
        dictionary.setProperty("Code Validation", "Sprawdzenie symbolu");
        dictionary.setProperty("Code Selection", "Wybór symbolu");

        // form...
        dictionary.setProperty("Confirm deliting data?", "Czy potwierdzasz usuniêcie danych?");
        dictionary.setProperty("Error while saving: incorrect data.", "Wyst¹pi³ b³¹d podczas zapisywania danych: nieprawid³owe dane.");
        dictionary.setProperty("Error on deleting:", "B³¹d podczas usuwania:");    //FIXME: ? "B³¹d usuwania"
        dictionary.setProperty("Error on Loading", "B³¹d ³adowania");
        dictionary.setProperty("Error while loading data:", "B³¹d podczas ³adowania danych:");
        dictionary.setProperty("Error on setting value to the input control having the attribute name", "Error on setting value to the input control having the attribute name");   //TODO


        // toolbar buttons...
        dictionary.setProperty("Delete record (CTRL+D)", "Usuñ (CTRL+D)");
        dictionary.setProperty("Edit record (CTRL+E)", "Zmieñ (CTRL+E)");
        dictionary.setProperty("New record (CTRL+I)", "Dopisz (CTRL+I)");
        dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)", "Odœwie¿/Zrezygnuj (CTRL+Z)");
        dictionary.setProperty("Save record (CTRL+S)", "Zapisz (CTRL+S)");
        dictionary.setProperty("Copy record (CTRL+C)", "Kopiuj (CTRL+C)");
        dictionary.setProperty("Export record (CTRL+X)", "Eksportuj (CTRL+X)");
        dictionary.setProperty("Import records (CTRL+M)", "Importuj (CTRL+M)");
        dictionary.setProperty("Load the first block of records", "Pocz¹tek danych");
        dictionary.setProperty("Select the previous row in grid", "Poprzednia pozycja");
        dictionary.setProperty("Select the next row in grid", "Nastêpna pozycja");
        dictionary.setProperty("Load the previous block of records", "Poprzedni blok danych");
        dictionary.setProperty("Load the next block of records", "Nastêpny blok danych");
        dictionary.setProperty("Load the last block of records", "Koniec danych");

        dictionary.setProperty("Insert", "Wstaw");
        dictionary.setProperty("Edit", "Zmieñ");
        dictionary.setProperty("Copy", "Kopiuj");
        dictionary.setProperty("Delete", "Usuñ");
        dictionary.setProperty("Save", "Zapisz");  //FIXME: ? "Zapamiêtaj"
        dictionary.setProperty("Reload", "Odœwie¿"); //FIXME: ? "Prze³aduj"
        dictionary.setProperty("Export", "Eksport");  //FIXME: ? "Eksportuj"
        dictionary.setProperty("Filter", "Filtr");  //FIXME: ? "Filtruj"

        // MDI Frame...
        dictionary.setProperty("file", "Program");
        dictionary.setProperty("exit", "Koniec");
        dictionary.setProperty("filemnemonic", "F");
        dictionary.setProperty("exitmnemonic", "E");
        dictionary.setProperty("change user", "Zmieñ u¿ytkownika");
        dictionary.setProperty("changeusermnemonic", "U");
        dictionary.setProperty("changelanguagemnemonic", "L");
        dictionary.setProperty("help", "Pomoc");
        dictionary.setProperty("about", "O programie");
        dictionary.setProperty("helpmnemonic", "H");
        dictionary.setProperty("aboutmnemonic", "A");
        dictionary.setProperty("are you sure to quit application?", "Czy zakoñczyæ dzia³anie aplikacji?");
        dictionary.setProperty("quit application", "Zamknij aplikacjê");
        dictionary.setProperty("forcegcmnemonic", "F");
        dictionary.setProperty("Force GC", "Uruchom GC");
        dictionary.setProperty("Java Heap", "Java Heap");
        dictionary.setProperty("used", "u¿yte");
        dictionary.setProperty("allocated", "przydzielone");
        dictionary.setProperty("change language", "Zmieñ jêzyk");
        dictionary.setProperty("changemnemonic", "L");
        dictionary.setProperty("cancelmnemonic", "C");
        dictionary.setProperty("cancel", "Zrezygnuj");
        dictionary.setProperty("yes", "Tak");
        dictionary.setProperty("no", "Nie");
        dictionary.setProperty("Functions", "Funkcje");
        dictionary.setProperty("Error while executing function", "B³¹d podczas wywo³ania funkcji");
        dictionary.setProperty("Error", "B³¹d");
        dictionary.setProperty("infoPanel", "Informacje");
        dictionary.setProperty("imageButton", "About");
        dictionary.setProperty("Window", "Okna");  //FIXME: ? "Okno"
        dictionary.setProperty("windowmnemonic", "W");
        dictionary.setProperty("Close All", "Zamknij wszystkie");
        dictionary.setProperty("closeallmnemonic", "A");
        dictionary.setProperty("Press ENTER to find function", "Naciœnij ENTER by znaleŸæ funkcjê");
        dictionary.setProperty("Find Function", "ZnajdŸ Funkcjê");
        dictionary.setProperty("Operation in progress...", "Operacja w toku...");
        dictionary.setProperty("close window", "Zamknij okno");
        dictionary.setProperty("reduce to icon", "Zmniejsz do ikony");
        dictionary.setProperty("are you sure to close this window?", "Czy jesteœ pewien ¿e chcesz zamkn¹c to okno?\nNiezapamiêtane zmiany zostan¹ utracone.");
        dictionary.setProperty("confirm window closing", "PotwierdŸ zamkniêcie okna");

        dictionary.setProperty("switch", "Prze³¹cz");
        dictionary.setProperty("switchmnemonic", "S");
        dictionary.setProperty("window name", "Nazwa okna");
        dictionary.setProperty("opened windows", "Otwarte okna");
        dictionary.setProperty("tile horizontally", "Uk³ad poziomy");
        dictionary.setProperty("tilehorizontallymnemonic", "H");
        dictionary.setProperty("tile vertically", "Uk³ad pionowy");
        dictionary.setProperty("tileverticallymnemonic", "V");
        dictionary.setProperty("cascade", "Kaskada");
        dictionary.setProperty("cascademnemonic", "C");
        dictionary.setProperty("minimize", "Minimalizuj"); //FIXME: ? "Zmniejsz"
        dictionary.setProperty("minimizemnemonic", "M");
        dictionary.setProperty("minimize all", "Minimalizuj wszystkie"); //FIXME: ? 'Zmniejsz wszystkie"
        dictionary.setProperty("minimizeallmnemonic", "A");

        // server...
        dictionary.setProperty("Client request not supported", "¯¹dania do klienta nie s¹ obs³ugiwane");
        dictionary.setProperty("User disconnected", "U¿ytkownik jest od³¹czony");
        dictionary.setProperty("Updating not performed: the record was previously updated.", "Aktualizacja siê nie powiod³a: rekord by³ juz uprzednio zaktualizowany.");

        // wizard...
        dictionary.setProperty("back", "Wstecz");
        dictionary.setProperty("next", "Dalej");
        dictionary.setProperty("finish", "Koniec");

        // image panel...
        dictionary.setProperty("image selection", "Wybór obrazu");

        // tip of the day panel...
        dictionary.setProperty("show 'tip of the day' after launching", "Pokazuj 'poradê dnia' po uruchomieniu aplikacji");
        dictionary.setProperty("previous tip", "Poprzednia");
        dictionary.setProperty("next tip", "Nastêpna");
        dictionary.setProperty("close", "Zamknij");
        dictionary.setProperty("tip of the day", "Porada dnia");

        // progress panel...
        dictionary.setProperty("progress", "Postêp");

        // licence agreement...
        dictionary.setProperty("i accept the terms in the licence agreement", "Akceptujê warunki zawarte w umowie licencyjnej");
        dictionary.setProperty("ok", "ZatwierdŸ");  //FIXME: ? "Ok"
        dictionary.setProperty("i do not accept the terms in the licence agreement", "Nie akceptujê warunków umowy licencyjnej");

        // property grid control
        dictionary.setProperty("property name", "Nazwa");
        dictionary.setProperty("property value", "Wartoœæ");

        // grid profile
        dictionary.setProperty("grid profile management", "Zarz¹dzanie profilami uk³adu tabeli");
        dictionary.setProperty("restore default grid profile", "Odtwórz domyœlny profil uk³adu tabeli");
        dictionary.setProperty("create new grid profile", "Utwórz nowy profil uk³adu tabeli");
        dictionary.setProperty("profile description", "Opis profilu");
        dictionary.setProperty("remove current grid profile", "Usuñ bierz¹cy profil uk³adu tabeli");
        dictionary.setProperty("select grid profile", "Wybierz profil uk³adu tabeli");
        dictionary.setProperty("default profile", "Profil domyœlny");

        // search box
        dictionary.setProperty("search", "Szukaj");
        dictionary.setProperty("not found", "Nie znaleziono");

        // drag...
        dictionary.setProperty("drag", "Ci¹gnij");

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
