package org.openswing.swing.internationalization.java;

import java.util.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieve the collection of all internationalization properties:
 * translations, data/numeric/currency formats.
 * No translation is performed, date/numeric/currency formats are based on italian formats.</p>
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
public class ItalianOnlyResourceFactory extends ResourcesFactory {

  /** internationalization settings */
  private Resources resources = null;


  /**
   * Constructor.
   * Currency symbol is based on euro.
   * @param additionalDictionary additional descriptions
   * @param showResourceNotFoundWarning warn when no resource key not found
   */
  public ItalianOnlyResourceFactory(Properties additionalDictionary,boolean showResourceNotFoundWarning) {
    Properties dictionary = new Properties();

    dictionary.putAll(additionalDictionary);

    // grid...
    dictionary.setProperty("of","di");
    dictionary.setProperty("page","Pagina");
    dictionary.setProperty("Remove Filter","Rimuovi filtro");
    dictionary.setProperty("This column is not sorteable","Questa colonna non è ordinabile");
    dictionary.setProperty("Sorting not allowed","Ordinamento non consentito");
    dictionary.setProperty("Maximum number of sorted columns","Massimo numero di colonne ordinate");
    dictionary.setProperty("Sorting not applicable","Ordinamento non applicabile");
    dictionary.setProperty("Selected Row","Riga selezionata");
    dictionary.setProperty("Selected Rows","Righe selezionate");
    dictionary.setProperty("Cancel changes and reload data?","Annullare le modifiche e ricaricare i dati?");
    dictionary.setProperty("Attention","Attenzione");
    dictionary.setProperty("Loading data...","Caricamento dati in corso...");
    dictionary.setProperty("Error while loading data","Errore durante il recupero dei dati");
    dictionary.setProperty("Loading Data Error","Errore di caricamento dati");
    dictionary.setProperty("Delete Rows?","Cancellare le righe?");
    dictionary.setProperty("Delete Confirmation","Conferma cancellazione");
    dictionary.setProperty("Error while deleting rows.","Errore durante la cancellazione delle righe.");
    dictionary.setProperty("Deleting Error","Errore di cancellazione");
    dictionary.setProperty("Error while saving","Errore durante il salvataggio dei dati");
    dictionary.setProperty("Saving Error","Errore in salvataggio");
    dictionary.setProperty("A mandatory column is empty.","Una colonna obbligatoria è vuota");
    dictionary.setProperty("Value not valid","Valore non valido");
    dictionary.setProperty("sorting conditions","Condizioni di ordinamento");
    dictionary.setProperty("filtering conditions","Condizioni di filtro");
    dictionary.setProperty("filtering and sorting settings","Impostazione di filtro e ordinamento");
    dictionary.setProperty("Filtering/Sorting data (CTRL+F)","Filtraggio/ordinamento dati (CTRL+F)");
    dictionary.setProperty("upload file","Upload File");
    dictionary.setProperty("download file","Download File");

    // export...
    dictionary.setProperty("grid export","Esportazione dati da griglia");
    dictionary.setProperty("export","Esportazione");
    dictionary.setProperty("exportmnemonic","E");
    dictionary.setProperty("column","Colonna");
    dictionary.setProperty("sel.","Sel.");
    dictionary.setProperty("you must select at least one column","E' necessario selezionare almeno una colonna");
    dictionary.setProperty("columns to export","Colonne da esportare");
    dictionary.setProperty("export type","Formato di esportazione");

    // import...
    dictionary.setProperty("grid import","Importazione dati in griglia");
    dictionary.setProperty("file to import","File da cui importare");
    dictionary.setProperty("import","Importazione");
    dictionary.setProperty("importmnemonic","I");
    dictionary.setProperty("columns to import","Colonne da importare");
    dictionary.setProperty("import type","Formato di importazione");
    dictionary.setProperty("error while importing data","Errore durante l'importazione dei dati");
    dictionary.setProperty("import completed","Importazione completata.");

    // quick filter...
    dictionary.setProperty("To value","Al valore");
    dictionary.setProperty("Filter by","Filtra per");
    dictionary.setProperty("From value","Dal valore");
    dictionary.setProperty("equals","uguale a");
    dictionary.setProperty("contains","contiene");
    dictionary.setProperty("starts with","inizia con");
    dictionary.setProperty("ends with","finisce con");

    // select/deselect all
    dictionary.setProperty("select all","Seleziona tutto");
    dictionary.setProperty("deselect all","Deseleziona tutto");

    // copy/cut/paste
    dictionary.setProperty("copy","Copia");
    dictionary.setProperty("copymnemonic","C");
    dictionary.setProperty("cut","Taglia");
    dictionary.setProperty("cutmnemonic","T");
    dictionary.setProperty("paste","Incolla");
    dictionary.setProperty("pastemnemonic","I");

    // lookup...
    dictionary.setProperty("Code is not correct.","Codice non valido.");
    dictionary.setProperty("Code Validation","Validazione codice");
    dictionary.setProperty("Code Selection","Selezione codice");

    // form...
    dictionary.setProperty("Confirm deliting data?","Conferma cancellazione dati?");
    dictionary.setProperty("Error while saving: incorrect data.","Errore durante il salvataggio: dati non corretti.");
    dictionary.setProperty("Error while validating data:","Errore durante la validazione dei dati:");
    dictionary.setProperty("Validation Error","Errore di validazione");
    dictionary.setProperty("Error on deleting:","Errore in cancellazione:");
    dictionary.setProperty("Error on Loading","Errore di caricamento");
    dictionary.setProperty("Error while loading data:","Errore durante il caricamento dati:");
    dictionary.setProperty("Error on setting value to the input control having the attribute name","Errore durante l'impostazione del contenuto del controllo di input avente nome attributo");

    // toolbar buttons...
    dictionary.setProperty("Delete record (CTRL+D)","Cancellazione record (CTRL+D)");
    dictionary.setProperty("Edit record (CTRL+E)","Modifica record (CTRL+E)");
    dictionary.setProperty("New record (CTRL+I)","Nuovo record (CTRL+I)");
    dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)","Ricarica record/annulla modifiche correnti (CTRL+Z)");
    dictionary.setProperty("Save record (CTRL+S)","Salva record (CTRL+S)");
    dictionary.setProperty("Copy record (CTRL+C)","Copia record (CTRL+C)");
    dictionary.setProperty("Export record (CTRL+X)","Esportazione records (CTRL+X)");
    dictionary.setProperty("Import records (CTRL+M)","Importazione records (CTRL+M)");
    dictionary.setProperty("Load the first block of records","Carica il primo blocco di records");
    dictionary.setProperty("Select the previous row in grid","Seleziona la prima riga in griglia");
    dictionary.setProperty("Select the next row in grid","Seleziona la riga successiva in griglia");
    dictionary.setProperty("Load the previous block of records","Carica il blocco di record precedente");
    dictionary.setProperty("Load the next block of records","Carica il blocco di record successivo");
    dictionary.setProperty("Load the last block of records","Carica l'ultimo blocco di record");

    dictionary.setProperty("Insert","Inserimento");
    dictionary.setProperty("Edit","Modifica");
    dictionary.setProperty("Copy","Copia");
    dictionary.setProperty("Delete","Cancellazione");
    dictionary.setProperty("Save","Salvataggio");
    dictionary.setProperty("Reload","Ricarica/Annulla");
    dictionary.setProperty("Export","Esportazione");
    dictionary.setProperty("Filter","Filtro");

    // MDI Frame...
    dictionary.setProperty("file","File");
    dictionary.setProperty("exit","Esci");
    dictionary.setProperty("filemnemonic","F");
    dictionary.setProperty("exitmnemonic","E");
    dictionary.setProperty("change user","Cambio utente");
    dictionary.setProperty("changeusermnemonic","U");
    dictionary.setProperty("changelanguagemnemonic","L");
    dictionary.setProperty("help","Help");
    dictionary.setProperty("about","Informazioni");
    dictionary.setProperty("helpmnemonic","H");
    dictionary.setProperty("aboutmnemonic","I");
    dictionary.setProperty("are you sure to quit application?","Conferma uscita dall'applicazione?");
    dictionary.setProperty("quit application","Termina applicazione");
    dictionary.setProperty("forcegcmnemonic","F");
    dictionary.setProperty("Force GC","Forza GC");
    dictionary.setProperty("Java Heap","Java Heap");
    dictionary.setProperty("used","usata");
    dictionary.setProperty("allocated","allocata");
    dictionary.setProperty("change language","Cambio lingua");
    dictionary.setProperty("changemnemonic","L");
    dictionary.setProperty("cancelmnemonic","A");
    dictionary.setProperty("cancel","Annulla");
    dictionary.setProperty("yes","Si");
    dictionary.setProperty("no","No");
    dictionary.setProperty("Functions","Funzioni");
    dictionary.setProperty("Error while executing function","Errore durante l'esecuzione della funzione");
    dictionary.setProperty("Error","Errore");
    dictionary.setProperty("infoPanel","Info");
    dictionary.setProperty("imageButton","About");
    dictionary.setProperty("Window","Finestre");
    dictionary.setProperty("windowmnemonic","F");
    dictionary.setProperty("Close All","Chiudi tutte le finestre");
    dictionary.setProperty("closeallmnemonic","C");
    dictionary.setProperty("closemnemonic","C");
    dictionary.setProperty("Press ENTER to find function","Premere INVIO per trovare una funzione");
    dictionary.setProperty("Find Function","Trova funzione");
    dictionary.setProperty("Operation in progress...","Elaborazione in corso...");
    dictionary.setProperty("close window","Chiudi finestra");
    dictionary.setProperty("reduce to icon","Riduci ad icona");
    dictionary.setProperty("save changes?", "Salvare i cambiamenti?");
    dictionary.setProperty("confirm window closing","Conferma chiusura finestra");
    dictionary.setProperty("change background","Cambio immagine di sfondo");
    dictionary.setProperty("reset background","Riprina sfondo predefinito");

    dictionary.setProperty("switch","Passa a");
    dictionary.setProperty("switchmnemonic","P");
    dictionary.setProperty("window name","Titolo finestra");
    dictionary.setProperty("opened windows","Finestre aperte");
    dictionary.setProperty("tile horizontally","Affianca orizzontalmente");
    dictionary.setProperty("tilehorizontallymnemonic","O");
    dictionary.setProperty("tile vertically","Affianca verticalmente");
    dictionary.setProperty("tileverticallymnemonic","V");
    dictionary.setProperty("cascade","Visualizza in cascata");
    dictionary.setProperty("cascademnemonic","C");
    dictionary.setProperty("minimize","Riduci ad icona");
    dictionary.setProperty("minimizemnemonic","R");
    dictionary.setProperty("minimize all","Riduci ad icona tutte le finestre");
    dictionary.setProperty("minimizeallmnemonic","T");
    dictionary.setProperty("selected frame","Finestra selezionata");

    // server...
    dictionary.setProperty("Client request not supported","Richiesta del client non supportata");
    dictionary.setProperty("User disconnected","Utente disconnnesso");
    dictionary.setProperty("Updating not performed: the record was previously updated.","Aggiornamento non eseguito: il record è stato precedentemente aggiornato.");

    // wizard...
    dictionary.setProperty("back","Indietro");
    dictionary.setProperty("next","Avanti");
    dictionary.setProperty("finish","Fine");

    // image panel...
    dictionary.setProperty("image selection","Selezione immagine");

    // tip of the day panel...
    dictionary.setProperty("show 'tip of the day' after launching","Mostra 'tip of the day' dopo l'avvio");
    dictionary.setProperty("previous tip","Tip precedente");
    dictionary.setProperty("next tip","Tip successivo");
    dictionary.setProperty("close","Chiudi");
    dictionary.setProperty("tip of the day","Tip of the day");
    dictionary.setProperty("select tip","Selezione Tip");
    dictionary.setProperty("tip name","Nome Tip");
    dictionary.setProperty("tips list","Lista Tips");

    // progress panel...
    dictionary.setProperty("progress","Avanzamento");

    // licence agreement...
    dictionary.setProperty("i accept the terms in the licence agreement","Accesso i termini della licenza");
    dictionary.setProperty("ok","Ok");
    dictionary.setProperty("i do not accept the terms in the licence agreement","Non accetto i termini della licenza");

    // property grid control
    dictionary.setProperty("property name","Nome");
    dictionary.setProperty("property value","Valore");

    // grid profile
    dictionary.setProperty("grid profile management","Gestione profilo griglia");
    dictionary.setProperty("restore default grid profile","Ripristina profilo griglia iniziale");
    dictionary.setProperty("create new grid profile","Crea nuovo profilo");
    dictionary.setProperty("profile description","Descrizione profilo");
    dictionary.setProperty("remove current grid profile","Rimuovi profilo corrente");
    dictionary.setProperty("select grid profile","Seleziona profilo di griglia");
    dictionary.setProperty("default profile","Profilo predefinito");

    // search box
    dictionary.setProperty("search","Ricerca");
    dictionary.setProperty("not found","Non trovato");

    // drag...
    dictionary.setProperty("drag","Drag");

    dictionary.setProperty("Caps lock pressed","Fissa maiuscole attivo");

    // pivot table...
    dictionary.setProperty("pivot table settings","Impostazione tabella Pivot");
    dictionary.setProperty("row fields","Campi riga");
    dictionary.setProperty("column fields","Campi colonna");
    dictionary.setProperty("data fields","Campi dati");
    dictionary.setProperty("filtering conditions","Condizioni di filtro");
    dictionary.setProperty("field","Campo");
    dictionary.setProperty("checked","Selez.");
    dictionary.setProperty("at least one data field must be selected","Almeno un campo data deve essere selezionato.");
    dictionary.setProperty("at least one row field must be selected","Almeno un campo riga deve essere selezionato.");
    dictionary.setProperty("at least one column field must be selected","Almeno un campo colonna deve essere selezionato.");
    dictionary.setProperty("expand all","Espandi tutto");
    dictionary.setProperty("collapse all","Riduci tutto");

    dictionary.setProperty(Consts.EQ,"Uguale a");
    dictionary.setProperty(Consts.GE,"Maggiore o uguale a");
    dictionary.setProperty(Consts.GT,"Maggiore di");
    dictionary.setProperty(Consts.IS_NOT_NULL,"Non è nullo");
    dictionary.setProperty(Consts.IS_NULL,"E' nullo");
    dictionary.setProperty(Consts.LE,"Minore o uguale a");
    dictionary.setProperty(Consts.LIKE,"Contiene");
    dictionary.setProperty(Consts.LT,"Minore di");
    dictionary.setProperty(Consts.NEQ,"Non uguale a");
    dictionary.setProperty(Consts.IN,"Contiene i valori");
    dictionary.setProperty(Consts.ASC_SORTED,"Ascendente");
    dictionary.setProperty(Consts.DESC_SORTED,"Discendente");
    dictionary.setProperty(Consts.NOT_IN,"Non contiene i valori");

    resources = new Resources(
      dictionary,
      "\u20AC",
      '.',
      ',',
      Resources.DMY,
      true,
      '/',
      "HH:mm",
      "IT",
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
      throw new UnsupportedOperationException("Language identifier not supported.");
  }


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public final Resources getResources(String langId) throws UnsupportedOperationException {
    if (!resources.getLanguageId().equals(langId))
    throw new UnsupportedOperationException("Language identifier not supported.");
    return resources;
  }



}
