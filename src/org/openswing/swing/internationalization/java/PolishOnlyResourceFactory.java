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
 * @author Mauro Carniel/Jacek Uznański
 * @version 1.0, based on version 1.7.7
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
        dictionary.setProperty("Remove Filter", "Usuń warunki wybierania");
        dictionary.setProperty("This column is not sorteable", "Ta kolumna nie podlega sortowaniu");
        dictionary.setProperty("Sorting not allowed", "Sortowanie jest niemożliwe");
        dictionary.setProperty("Maximum number of sorted columns", "Wykorzystano maksymalną liczbę sortowanych kolumn");
        dictionary.setProperty("Sorting not applicable", "Ograniczenie sortowania");
        dictionary.setProperty("Selected Row", "Wybrana pozycja");
        dictionary.setProperty("Selected Rows", "Wybrana pozycje");
        dictionary.setProperty("Cancel changes and reload data?", "Czy porzucić zmiany i przeładować dane?");
        dictionary.setProperty("Attention", "Uwaga");
        dictionary.setProperty("Loading data...", "Trwa pobieranie danych...");
        dictionary.setProperty("Error while loading data", "Wystąpił błąd podczas pobierania danych");
        dictionary.setProperty("Loading Data Error", "Błąd pobierania danych");
        dictionary.setProperty("Delete Rows?", "Czy usunąć wskazane pozycje?");
        dictionary.setProperty("Delete Confirmation", "Potwierdzenie usunięcia");
        dictionary.setProperty("Error while deleting rows.", "Wystąpił błąd podczas usuwania danych.");
        dictionary.setProperty("Deleting Error", "Błąd kasowania danych");
        dictionary.setProperty("Error while saving", "Wystąpił błąd podczas zapisywania danych.");
        dictionary.setProperty("Saving Error", "Błąd zapisywania danych");
        dictionary.setProperty("A mandatory column is empty.", "Brak wartości dla wymaganej kolumny.");
        dictionary.setProperty("Value not valid", "Nieprawidłowa wartość");
        dictionary.setProperty("sorting conditions", "Warunki sortowania");
        dictionary.setProperty("filtering conditions", "Warunki wybierania");
        dictionary.setProperty("filtering and sorting settings", "Warunki wybierania i sortowania");
        dictionary.setProperty("Filtering/Sorting data (CTRL+F)", "Warunki wybierania i sortowania (CTRL+F)");
        dictionary.setProperty("upload file", "Wysłanie pliku");
        dictionary.setProperty("download file", "Pobranie pliku");

        // export...
        dictionary.setProperty("grid export", "Eksport danych z tabeli");
        dictionary.setProperty("export", "Eksportuj");
        dictionary.setProperty("exportmnemonic", "X");
        dictionary.setProperty("column", "Kolumna");
        dictionary.setProperty("sel.", "Wyb.");
        dictionary.setProperty("you must select at least one column", "Musisz wybrać co najmniej jedną kolumnę");
        dictionary.setProperty("columns to export", "Kolumny do wyeksportowania");
        dictionary.setProperty("export type", "Format eksportu");

        // import...
        dictionary.setProperty("grid import", "Import danych do tabeli");
        dictionary.setProperty("file to import", "Plik do zaimportowania");
        dictionary.setProperty("import", "Importuj");
        dictionary.setProperty("importmnemonic", "M");
        dictionary.setProperty("columns to import", "Kolumny do zaimportowania");
        dictionary.setProperty("import type", "Format importu");
        dictionary.setProperty("error while importing data", "Wystąpił błąd podczas importowania danych");
        dictionary.setProperty("import completed", "Importowanie zakończone");

        // quick filter...
        dictionary.setProperty("To value", "Do wartości");
        dictionary.setProperty("Filter by", "Wybierz dla");
        dictionary.setProperty("From value", "Od wartości");
        dictionary.setProperty("equals", "równe");
        dictionary.setProperty("contains", "zawiera");
        dictionary.setProperty("starts with", "zaczyna się");
        dictionary.setProperty("ends with", "kończy się");

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
        dictionary.setProperty("Code is not correct.", "Podany symbol jest nieprawidłowy.");
        dictionary.setProperty("Code Validation", "Sprawdzenie symbolu");
        dictionary.setProperty("Code Selection", "Wybór symbolu");

        // form...
        dictionary.setProperty("Confirm deliting data?", "Czy potwierdzasz usunięcie danych?");
        dictionary.setProperty("Error while saving: incorrect data.", "Wystąpił błąd podczas zapisywania danych: nieprawidłowe dane.");
        dictionary.setProperty("Error on deleting:", "Błąd podczas usuwania:");  
        dictionary.setProperty("Error on Loading", "Błąd podczas ładowania");
        dictionary.setProperty("Error while loading data:", "Wystąpił błąd podczas ładowania danych:");
        dictionary.setProperty("Error on setting value to the input control having the attribute name", "Błąd nadania wartości elementowi edycji dla atrybutu");


        // toolbar buttons...
        dictionary.setProperty("Delete record (CTRL+D)", "Usuń (CTRL+D)");
        dictionary.setProperty("Edit record (CTRL+E)", "Zmień (CTRL+E)");
        dictionary.setProperty("New record (CTRL+I)", "Dopisz (CTRL+I)");
        dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)", "Odśwież/Zrezygnuj (CTRL+Z)");
        dictionary.setProperty("Save record (CTRL+S)", "Zapisz (CTRL+S)");
        dictionary.setProperty("Copy record (CTRL+C)", "Kopiuj (CTRL+C)");
        dictionary.setProperty("Export record (CTRL+X)", "Eksportuj (CTRL+X)");
        dictionary.setProperty("Import records (CTRL+M)", "Importuj (CTRL+M)");
        dictionary.setProperty("Load the first block of records", "Początek danych");
        dictionary.setProperty("Select the previous row in grid", "Poprzednia pozycja");
        dictionary.setProperty("Select the next row in grid", "Następna pozycja");
        dictionary.setProperty("Load the previous block of records", "Poprzedni blok danych");
        dictionary.setProperty("Load the next block of records", "Następny blok danych");
        dictionary.setProperty("Load the last block of records", "Koniec danych");

        dictionary.setProperty("Insert", "Wstaw");
        dictionary.setProperty("Edit", "Zmień");
        dictionary.setProperty("Copy", "Kopiuj");
        dictionary.setProperty("Delete", "Usuń");
        dictionary.setProperty("Save", "Zapisz");
        dictionary.setProperty("Reload", "Odśwież");
        dictionary.setProperty("Export", "Eksportuj");
        dictionary.setProperty("Filter", "Filtruj");

        // MDI Frame...
        dictionary.setProperty("file", "Program");
        dictionary.setProperty("exit", "Koniec");
        dictionary.setProperty("filemnemonic", "F");
        dictionary.setProperty("exitmnemonic", "E");
        dictionary.setProperty("change user", "Zmień użytkownika");
        dictionary.setProperty("changeusermnemonic", "U");
        dictionary.setProperty("changelanguagemnemonic", "L");
        dictionary.setProperty("help", "Pomoc");
        dictionary.setProperty("about", "O programie");
        dictionary.setProperty("helpmnemonic", "H");
        dictionary.setProperty("aboutmnemonic", "A");
        dictionary.setProperty("are you sure to quit application?", "Czy zakończyć działanie aplikacji?");
        dictionary.setProperty("quit application", "Zamknij aplikację");
        dictionary.setProperty("forcegcmnemonic", "F");
        dictionary.setProperty("Force GC", "Uruchom GC");
        dictionary.setProperty("Java Heap", "Java Heap");
        dictionary.setProperty("used", "użyte");
        dictionary.setProperty("allocated", "przydzielone");
        dictionary.setProperty("change language", "Zmień język");
        dictionary.setProperty("changemnemonic", "L");
        dictionary.setProperty("cancelmnemonic", "C");
        dictionary.setProperty("cancel", "Zrezygnuj");
        dictionary.setProperty("yes", "Tak");
        dictionary.setProperty("no", "Nie");
        dictionary.setProperty("Functions", "Funkcje");
        dictionary.setProperty("Error while executing function", "Błąd podczas wywołania funkcji");
        dictionary.setProperty("Error", "Błąd");
        dictionary.setProperty("infoPanel", "Informacje");
        dictionary.setProperty("imageButton", "About");
        dictionary.setProperty("Window", "Okna");
        dictionary.setProperty("windowmnemonic", "W");
        dictionary.setProperty("Close All", "Zamknij wszystkie");
        dictionary.setProperty("closeallmnemonic", "A");
        dictionary.setProperty("Press ENTER to find function", "Naciśnij ENTER by znaleźć funkcję");
        dictionary.setProperty("Find Function", "Znajdź Funkcję");
        dictionary.setProperty("Operation in progress...", "Operacja w toku...");
        dictionary.setProperty("close window", "Zamknij okno");
        dictionary.setProperty("reduce to icon", "Zmniejsz do ikony");
        dictionary.setProperty("are you sure to close this window?", "Czy jesteś pewien że chcesz zamknąc to okno?\nNiezapamiętane zmiany zostaną utracone.");
        dictionary.setProperty("confirm window closing", "Potwierdź zamknięcie okna");
        dictionary.setProperty("change background","Zmień tło");
        dictionary.setProperty("reset background","Przywróć tło");

        dictionary.setProperty("switch", "Przełącz");
        dictionary.setProperty("switchmnemonic", "S");
        dictionary.setProperty("window name", "Nazwa okna");
        dictionary.setProperty("opened windows", "Otwarte okna");
        dictionary.setProperty("tile horizontally", "Układ poziomy");
        dictionary.setProperty("tilehorizontallymnemonic", "H");
        dictionary.setProperty("tile vertically", "Układ pionowy");
        dictionary.setProperty("tileverticallymnemonic", "V");
        dictionary.setProperty("cascade", "Kaskada");
        dictionary.setProperty("cascademnemonic", "C");
        dictionary.setProperty("minimize", "Minimalizuj");
        dictionary.setProperty("minimizemnemonic", "M");
        dictionary.setProperty("minimize all", "Minimalizuj wszystkie");
        dictionary.setProperty("minimizeallmnemonic", "A");

        // server...
        dictionary.setProperty("Client request not supported", "Żądania do klienta nie są obsługiwane");
        dictionary.setProperty("User disconnected", "Użytkownik jest odłączony");
        dictionary.setProperty("Updating not performed: the record was previously updated.", "Aktualizacja się nie powiodła: rekord był juz uprzednio zaktualizowany.");

        // wizard...
        dictionary.setProperty("back", "Wstecz");
        dictionary.setProperty("next", "Dalej");
        dictionary.setProperty("finish", "Koniec");

        // image panel...
        dictionary.setProperty("image selection", "Wybór obrazu");

        // tip of the day panel...
        dictionary.setProperty("show 'tip of the day' after launching", "Pokazuj 'poradę dnia' po uruchomieniu aplikacji");
        dictionary.setProperty("previous tip", "Poprzednia");
        dictionary.setProperty("next tip", "Następna");
        dictionary.setProperty("close", "Zamknij");
        dictionary.setProperty("tip of the day", "Porada dnia");

        // progress panel...
        dictionary.setProperty("progress", "Postęp");

        // licence agreement...
        dictionary.setProperty("i accept the terms in the licence agreement", "Akceptuję warunki zawarte w umowie licencyjnej");
        dictionary.setProperty("ok", "Zatwierdź");
        dictionary.setProperty("i do not accept the terms in the licence agreement", "Nie akceptuję warunków umowy licencyjnej");

        // property grid control
        dictionary.setProperty("property name", "Nazwa");
        dictionary.setProperty("property value", "Wartość");

        // grid profile
        dictionary.setProperty("grid profile management", "Zarządzanie profilami układu tabeli");
        dictionary.setProperty("restore default grid profile", "Odtwórz domyślny profil układu tabeli");
        dictionary.setProperty("create new grid profile", "Utwórz nowy profil układu tabeli");
        dictionary.setProperty("profile description", "Opis profilu");
        dictionary.setProperty("remove current grid profile", "Usuń bierzący profil układu tabeli");
        dictionary.setProperty("select grid profile", "Wybierz profil układu tabeli");
        dictionary.setProperty("default profile", "Profil domyślny");

        // search box
        dictionary.setProperty("search", "Szukaj");
        dictionary.setProperty("not found", "Nie znaleziono");

        // drag...
        dictionary.setProperty("drag", "Ciągnij");

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
