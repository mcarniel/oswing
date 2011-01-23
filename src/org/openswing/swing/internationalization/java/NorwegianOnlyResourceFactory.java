package org.openswing.swing.internationalization.java;

import java.util.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieve the collection of all internationalization properties:
 * translations, data/numeric/currency formats.
 * No translation is performed, date/numeric/currency formats are based on norwegian formats.</p>
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
 * @author Mauro Carniel
 * @version 1.0
 */
public class NorwegianOnlyResourceFactory extends ResourcesFactory {

  /** internationalization settings */
  private Resources resources = null;


  /**
   * Constructor.
   * @param currencySymbol currency symbol
   * @param additionalDictionary additional descriptions
   * @param showResourceNotFoundWarning warn when no resource key not found
   */
  public NorwegianOnlyResourceFactory(String currencySymbol,Properties additionalDictionary,boolean showResourceNotFoundWarning) {
    this(currencySymbol,additionalDictionary,showResourceNotFoundWarning,'/');
  }



  /**
   * Constructor.
   * @param currencySymbol currency symbol
   * @param additionalDictionary additional descriptions
   * @param showResourceNotFoundWarning warn when no resource key not found
   * @param dateFormatSeparator date format separator; for example: '-' or '/'
   */
  public NorwegianOnlyResourceFactory(String currencySymbol,Properties additionalDictionary,boolean showResourceNotFoundWarning,char dateFormatSeparator) {
    Properties dictionary = new Properties();

    dictionary.putAll(additionalDictionary);

    // grid...
    dictionary.setProperty("of","av");
    dictionary.setProperty("page","Side");
    dictionary.setProperty("Remove Filter","Fjern Filter");
    dictionary.setProperty("This column is not sorteable","Denne kolonnen kan ikke sorteres");
    dictionary.setProperty("Sorting not allowed","Sortering ikke tillatt");
    dictionary.setProperty("Maximum number of sorted columns","Maksimalt antall av sorterte kolonner");
    dictionary.setProperty("Sorting not applicable","Sortering er ikke mulig");
    dictionary.setProperty("Selected Row","Valgt Rad");
    dictionary.setProperty("Selected Rows","Valgte Rader");
    dictionary.setProperty("Cancel changes and reload data?","Avbryt Endringer og hent data på nytt?");
    dictionary.setProperty("Attention","Advarsel");
    dictionary.setProperty("Loading data...","Henter data...");
    dictionary.setProperty("Error while loading data","Feil under henting av data");
    dictionary.setProperty("Loading Data Error","Feil ved henting av data på nytt");
    dictionary.setProperty("Delete Rows?","Slett Rader?");
    dictionary.setProperty("Delete Confirmation","Slett Bekreftelse");
    dictionary.setProperty("Error while deleting rows.","Feil under sletting av rader.");
    dictionary.setProperty("Deleting Error","Slette Feil");
    dictionary.setProperty("Error while saving","Feil under lagring");
    dictionary.setProperty("Saving Error","Lagrings Feil");
    dictionary.setProperty("A mandatory column is empty.","En obligatorisk kolonne er tom");
    dictionary.setProperty("Value not valid","Verdi er ikke gyldig");
    dictionary.setProperty("sorting conditions","Sorterings betingelser");
    dictionary.setProperty("filtering conditions","Filtrerings betingelser");
    dictionary.setProperty("filtering and sorting settings","Filtrering and sorterings innstillinger");
    dictionary.setProperty("Filtering/Sorting data (CTRL+F)","Filtrering/Sortering av data (CTRL+F)");
    dictionary.setProperty("upload file","Last opp File");
    dictionary.setProperty("download file","Last Ned File");

    // export...
    dictionary.setProperty("grid export","Tabell Eksport");
    dictionary.setProperty("export","Eksport");
    dictionary.setProperty("exportmnemonic","X");
    dictionary.setProperty("column","Kolonne");
    dictionary.setProperty("sel.","Velg");
    dictionary.setProperty("you must select at least one column","Du må velge minst en kolonne");
    dictionary.setProperty("columns to export","Kolonner for eksport");
    dictionary.setProperty("export type","Eksport format");

    // import...
    dictionary.setProperty("grid import","Tabell Import");
    dictionary.setProperty("file to import","File for å importere");
    dictionary.setProperty("import","Import");
    dictionary.setProperty("importmnemonic","M");
    dictionary.setProperty("columns to import","Kolonner for import");
    dictionary.setProperty("import type","Import format");
    dictionary.setProperty("error while importing data","Feil med import av data");
    dictionary.setProperty("import completed","Import ferdig.");

    // quick filter...
    dictionary.setProperty("To value","Til verdi");
    dictionary.setProperty("Filter by","Filtrer ved");
    dictionary.setProperty("From value","Fra verdi");
    dictionary.setProperty("equals","lik");
    dictionary.setProperty("contains","inneholder");
    dictionary.setProperty("starts with","starter med");
    dictionary.setProperty("ends with","ender med");

    // select/deselect all
    dictionary.setProperty("select all","Velg alle");
    dictionary.setProperty("deselect all","Velg bort alle");

    // copy/cut/paste
    dictionary.setProperty("copy","Kopier");
    dictionary.setProperty("copymnemonic","K");
    dictionary.setProperty("cut","Kutt");
    dictionary.setProperty("cutmnemonic","U");
    dictionary.setProperty("paste","Lim inn");
    dictionary.setProperty("pastemnemonic","L");

    // lookup...
    dictionary.setProperty("Code is not correct.","Kode er ikke korrekt.");
    dictionary.setProperty("Code Validation","Kode Validering");
    dictionary.setProperty("Code Selection","Kode Valg");

    // form...
    dictionary.setProperty("Confirm deliting data?","Bekreft sletting av data?");
    dictionary.setProperty("Error while saving: incorrect data.","Feil under lagring: ukorrekte data.");
    dictionary.setProperty("Error while validating data:","Feil under validering av data:");
    dictionary.setProperty("Validation Error","Validering feil");
    dictionary.setProperty("Error on deleting:","Feil under sletting:");
    dictionary.setProperty("Error on Loading","Feil under Henting");
    dictionary.setProperty("Error while loading data:","Feil under henting av data:");
    dictionary.setProperty("Error on setting value to the input control having the attribute name","Feil ved setting av verdi for input kontroll med attributt navn");

    // toolbar buttons...
    dictionary.setProperty("Delete record (CTRL+D)","Slett post (CTRL+D)");
    dictionary.setProperty("Edit record (CTRL+E)","Editer post (CTRL+E)");
    dictionary.setProperty("New record (CTRL+I)","Ny post (CTRL+I)");
    dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)","Hent post på nytt/Avbryt pågående operasjon  (CTRL+Z)");
    dictionary.setProperty("Save record (CTRL+S)","Lagre post (CTRL+S)");
    dictionary.setProperty("Copy record (CTRL+C)","Kopier post (CTRL+C)");
    dictionary.setProperty("Export record (CTRL+X)","Eksporter poster (CTRL+X)");
    dictionary.setProperty("Import records (CTRL+M)","Importer poster (CTRL+M)");
    dictionary.setProperty("Load the first block of records","Hent første blokk of poster");
    dictionary.setProperty("Select the previous row in grid","Velg forrige rad i tabell");
    dictionary.setProperty("Select the next row in grid","Velg neste rad i tabell");
    dictionary.setProperty("Load the previous block of records","Last forrige blokk av poster");
    dictionary.setProperty("Load the next block of records","Hent neste blokk av poster");
    dictionary.setProperty("Load the last block of records","Hent den siste blokk av poster");

    dictionary.setProperty("Insert","Sett inn");
    dictionary.setProperty("Edit","Editer");
    dictionary.setProperty("Copy","Kopier");
    dictionary.setProperty("Delete","Slett");
    dictionary.setProperty("Save","Lagre");
    dictionary.setProperty("Reload","Hent");
    dictionary.setProperty("Export","Eksporter");
    dictionary.setProperty("Filter","Filter");

    // MDI Frame...
    dictionary.setProperty("file","File");
    dictionary.setProperty("exit","Avslutt");
    dictionary.setProperty("filemnemonic","F");
    dictionary.setProperty("exitmnemonic","A");
    dictionary.setProperty("change user","Endre Bruker");
    dictionary.setProperty("changeusermnemonic","U");
    dictionary.setProperty("changelanguagemnemonic","S");
    dictionary.setProperty("help","Hjelp");
    dictionary.setProperty("about","Om");
    dictionary.setProperty("helpmnemonic","H");
    dictionary.setProperty("aboutmnemonic","O");
    dictionary.setProperty("are you sure to quit application?","Er du sikker på å avslutte program?");
    dictionary.setProperty("quit application","Avslutt program");
    dictionary.setProperty("forcegcmnemonic","T");
    dictionary.setProperty("Force GC","Tving GC");
    dictionary.setProperty("Java Heap","Java Minne");
    dictionary.setProperty("used","brukt");
    dictionary.setProperty("allocated","allokert");
    dictionary.setProperty("change language","Endre Språk");
    dictionary.setProperty("changemnemonic","S");
    dictionary.setProperty("cancelmnemonic","A");
    dictionary.setProperty("cancel","Avbryt");
    dictionary.setProperty("yes","Ja");
    dictionary.setProperty("no","Nei");
    dictionary.setProperty("Functions","Funksjoner");
    dictionary.setProperty("Error while executing function","Feil under eksekvering av funksjon");
    dictionary.setProperty("Error","Feil");
    dictionary.setProperty("infoPanel","Info");
    dictionary.setProperty("imageButton","Om");
    dictionary.setProperty("Window","Vindu");
    dictionary.setProperty("windowmnemonic","V");
    dictionary.setProperty("Close All","Lukk Alt");
    dictionary.setProperty("closeallmnemonic","A");
    dictionary.setProperty("closemnemonic","A");
    dictionary.setProperty("Press ENTER to find function","Trykk ENTER for å finne funksjon");
    dictionary.setProperty("Find Function","Finn Funksjon");
    dictionary.setProperty("Operation in progress...","Operasjon under utførelse...");
    dictionary.setProperty("close window","Lukk Vindu");
    dictionary.setProperty("reduce to icon","Reduser til ikon");
    dictionary.setProperty("save changes?", "Lagre endringer?");
    dictionary.setProperty("confirm window closing","Bekreft stenging av vindu");
    dictionary.setProperty("change background","Endre bakgrunn");
    dictionary.setProperty("reset background","Tibakestill bakgrunn");

    dictionary.setProperty("switch","Bytt");
    dictionary.setProperty("switchmnemonic","S");
    dictionary.setProperty("window name","Vindu navn");
    dictionary.setProperty("opened windows","Åpne vinduer");
    dictionary.setProperty("tile horizontally","Ordne horisontalt");
    dictionary.setProperty("tilehorizontallymnemonic","H");
    dictionary.setProperty("tile vertically","Ordne vertikalt");
    dictionary.setProperty("tileverticallymnemonic","V");
    dictionary.setProperty("cascade","Overlappe");
    dictionary.setProperty("cascademnemonic","C");
    dictionary.setProperty("minimize","Minimer");
    dictionary.setProperty("minimizemnemonic","M");
    dictionary.setProperty("minimize all","Minimer alle");
    dictionary.setProperty("minimizeallmnemonic","A");

    // server...
    dictionary.setProperty("Client request not supported","Klient forespørsel ikke tillatt");
    dictionary.setProperty("User disconnected","Bruker frakoblet");
    dictionary.setProperty("Updating not performed: the record was previously updated.","Oppdatering ikke utført: posten har blitt oppdatert tidligere.");

    // wizard...
    dictionary.setProperty("back","Tilbake");
    dictionary.setProperty("next","Neste");
    dictionary.setProperty("finish","Ferdig");

    // image panel...
    dictionary.setProperty("image selection","Bilde valg");

    // tip of the day panel...
    dictionary.setProperty("show 'tip of the day' after launching","Vis 'dagens tips' etter oppstart");
    dictionary.setProperty("previous tip","Forrige tips");
    dictionary.setProperty("next tip","Neste tips");
    dictionary.setProperty("close","Lukk");
    dictionary.setProperty("tip of the day","Dagens tips");
    dictionary.setProperty("select tip","Velg tip");
    dictionary.setProperty("tip name","Tips navn");
    dictionary.setProperty("tips list","Tips list");

    // progress panel...
    dictionary.setProperty("progress","Framdrift");

    // licence agreement...
    dictionary.setProperty("i accept the terms in the licence agreement","Jeg aksepteter lisensbetingelsene");
    dictionary.setProperty("ok","Ok");
    dictionary.setProperty("i do not accept the terms in the licence agreement","Jeg aksepteter ikke lisensbetingelsene");

    // property grid control
    dictionary.setProperty("property name","Navn");
    dictionary.setProperty("property value","Verdi");

    // grid profile
    dictionary.setProperty("grid profile management","Tabell profil administrasjon");
    dictionary.setProperty("restore default grid profile","Tilbakestill til standard tabell profil");
    dictionary.setProperty("create new grid profile","Opprett ny tabell profil");
    dictionary.setProperty("profile description","Profil beskrivelse");
    dictionary.setProperty("remove current grid profile","Fjern nåværende tabell profil");
    dictionary.setProperty("select grid profile","Velg tabell profil");
    dictionary.setProperty("default profile","Standard profil");

    // search box
    dictionary.setProperty("search","Søk");
    dictionary.setProperty("not found","Ikke funnet");

    // drag...
    dictionary.setProperty("drag","Dra");

    dictionary.setProperty("Caps lock pressed","Caps lock trykkes");

    // pivot table...
    dictionary.setProperty("pivot table settings","Pivot tabell innstillinger");
    dictionary.setProperty("row fields","Rad felter");
    dictionary.setProperty("column fields","Kolonne  felt");
    dictionary.setProperty("data fields","Data felter");
    dictionary.setProperty("filtering conditions","Filtrerings betingeler");
    dictionary.setProperty("field","Felt");
    dictionary.setProperty("checked","Kontrollert");
    dictionary.setProperty("at least one data field must be selected","Minst et datafelt må bli valgt.");
    dictionary.setProperty("at least one row field must be selected","Minst en rad må bli valgt.");
    dictionary.setProperty("at least one column field must be selected","Minst en kolonne må bli valgt.");
    dictionary.setProperty("expand all","Ekspandere alle");
    dictionary.setProperty("collapse all","Kollapse alle");

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
      Resources.DMY,
      true,
      dateFormatSeparator,
      "HH:mm",
      "NO",
      showResourceNotFoundWarning
    );
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
    if (!resources.getLanguageId().equals(langId))
      throw new UnsupportedOperationException("Språk identifikator ikke støttet.");
  }


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public final Resources getResources(String langId) throws UnsupportedOperationException {
    if (!resources.getLanguageId().equals(langId))
    throw new UnsupportedOperationException("Språk identifikator  ikke støttet..");
    return resources;
  }



}
