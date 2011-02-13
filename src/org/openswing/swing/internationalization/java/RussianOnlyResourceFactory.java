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
 * @author Mauro Carniel
 * @version 1.0
 */
public class RussianOnlyResourceFactory extends ResourcesFactory {

  /** internationalization settings */
  private Resources resources = null;


  /**
   * Constructor.
   * @param currencySymbol currency symbol
   * @param additionalDictionary additional descriptions
   * @param showResourceNotFoundWarning warn when no resource key not found
   */
  public RussianOnlyResourceFactory(String currencySymbol,Properties additionalDictionary,boolean showResourceNotFoundWarning) {
    this(currencySymbol,additionalDictionary,showResourceNotFoundWarning,'/');
  }



  /**
   * Constructor.
   * @param currencySymbol currency symbol
   * @param additionalDictionary additional descriptions
   * @param showResourceNotFoundWarning warn when no resource key not found
   * @param dateFormatSeparator date format separator; for example: '-' or '/'
   */
  public RussianOnlyResourceFactory(String currencySymbol,Properties additionalDictionary,boolean showResourceNotFoundWarning,char dateFormatSeparator) {
    Properties dictionary = new Properties();

    dictionary.putAll(additionalDictionary);

    // grid...
    dictionary.setProperty("of","из");
    dictionary.setProperty("page","Страница");
    dictionary.setProperty("Remove Filter","Удалить фильтр");
    dictionary.setProperty("This column is not sorteable","Сортировка по данному полю не возможна");
    dictionary.setProperty("Sorting not allowed","Сортировка не возможна");
    dictionary.setProperty("Maximum number of sorted columns","Максимальное количество полей сортировки");
    dictionary.setProperty("Sorting not applicable","Сортировка не применяется");
    dictionary.setProperty("Selected Row","Строка");
    dictionary.setProperty("Selected Rows","Строки");
    dictionary.setProperty("Cancel changes and reload data?","Отменить изменения и перезагрузить данные?");
    dictionary.setProperty("Attention","Внимание");
    dictionary.setProperty("Loading data...","Загрузка данных...");
    dictionary.setProperty("Error while loading data","Ошибка при загрузке данных");
    dictionary.setProperty("Loading Data Error","Ошибка загрузки данных");
    dictionary.setProperty("Delete Rows?","Удалить строки?");
    dictionary.setProperty("Delete Confirmation","Подтверждение удаления");
    dictionary.setProperty("Error while deleting rows.","Ошибка при удалении строк.");
    dictionary.setProperty("Deleting Error","Ошибка удаления");
    dictionary.setProperty("Error while saving","Ошибка при сохранении");
    dictionary.setProperty("Saving Error","Ошибка сохранения");
    dictionary.setProperty("A mandatory column is empty.","Обязательное поле не заполнено");
    dictionary.setProperty("Value not valid","Значение не корректно");
    dictionary.setProperty("sorting conditions","Условия сортировки");
    dictionary.setProperty("filtering conditions","Условия фильтрации");
    dictionary.setProperty("filtering and sorting settings","Настройки фильтрации и сортировки");
    dictionary.setProperty("Filtering/Sorting data (CTRL+F)","Фильтрация/Сортировка данных (CTRL+F)");
    dictionary.setProperty("upload file","Загрузить файл");
    dictionary.setProperty("download file","Скачать файл");

    // export...
    dictionary.setProperty("grid export","Таблица экспорта");
    dictionary.setProperty("export","Экспорт");
    dictionary.setProperty("exportmnemonic","X");
    dictionary.setProperty("column","Столбец");
    dictionary.setProperty("sel.","Sel.");
    dictionary.setProperty("you must select at least one column","Вы должны выбрать хотя бы один столбец.");
    dictionary.setProperty("columns to export","Экспортируемые столбцы");
    dictionary.setProperty("export type","Формат экспорта");

    // import...
    dictionary.setProperty("grid import","Таблица импорта");
    dictionary.setProperty("file to import","Импортируемый файл");
    dictionary.setProperty("import","Импорт");
    dictionary.setProperty("importmnemonic","M");
    dictionary.setProperty("columns to import","Столбцы импорта");
    dictionary.setProperty("import type","Формат импорта");
    dictionary.setProperty("error while importing data","Ошибка при импорте данных");
    dictionary.setProperty("import completed","Импорт закончен.");

    // quick filter...
    dictionary.setProperty("To value","От значения");
    dictionary.setProperty("Filter by","Фильтровать по");
    dictionary.setProperty("From value","До значения");
    dictionary.setProperty("equals","равно");
    dictionary.setProperty("contains","содержит");
    dictionary.setProperty("starts with","начинается с");
    dictionary.setProperty("ends with","заканчивается на");

    // select/deselect all
    dictionary.setProperty("select all","Выделить все");
    dictionary.setProperty("deselect all","Сбросить выделение");

    // copy/cut/paste
    dictionary.setProperty("copy","Копировать");
    dictionary.setProperty("copymnemonic","C");
    dictionary.setProperty("cut","Вырезать");
    dictionary.setProperty("cutmnemonic","U");
    dictionary.setProperty("paste","Вставить");
    dictionary.setProperty("pastemnemonic","P");

    // lookup...
    dictionary.setProperty("Code is not correct.","Код не корректен.");
    dictionary.setProperty("Code Validation","Проверка кода");
    dictionary.setProperty("Code Selection","Выбор кода");

    dictionary.setProperty("Caps lock pressed","Включен верхний регистр");

    // form...
    dictionary.setProperty("Confirm deliting data?","Подтверждаете удаление данных?");
    dictionary.setProperty("Error while saving: incorrect data.","Ошибка при сохранении: некорректные данные.");
    dictionary.setProperty("Error while validating data:","Ошибка при проверке данных:");
    dictionary.setProperty("Validation Error","Ошибка при проверке");
    dictionary.setProperty("Error on deleting:","Ошибка при удалении:");
    dictionary.setProperty("Error on Loading","Ошибка при загрузке");
    dictionary.setProperty("Error while loading data:","Ошибка при загрузке данных:");
    dictionary.setProperty("Error on setting value to the input control having the attribute name","Ошибка при выборе значения элемента управления");

    // toolbar buttons...
    dictionary.setProperty("Delete record (CTRL+D)","Удалить строку (CTRL+D)");
    dictionary.setProperty("Edit record (CTRL+E)","Редактировать строку (CTRL+E)");
    dictionary.setProperty("New record (CTRL+I)","Новая строка (CTRL+I)");
    dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)","Перезагрузить/Сбросить изменения (CTRL+Z)");
    dictionary.setProperty("Save record (CTRL+S)","Сохранить строку (CTRL+S)");
    dictionary.setProperty("Copy record (CTRL+C)","Копировать строку (CTRL+C)");
    dictionary.setProperty("Export record (CTRL+X)","Экспорт строк (CTRL+X)");
    dictionary.setProperty("Import records (CTRL+M)","Импорт строк (CTRL+M)");
    dictionary.setProperty("Load the first block of records","Загрузить первый блок строк");
    dictionary.setProperty("Select the previous row in grid","Выбрать предыдущую строку таблицы");
    dictionary.setProperty("Select the next row in grid","Выбрать следующую строку таблицы");
    dictionary.setProperty("Load the previous block of records","Загрузить предыдущий блок строк");
    dictionary.setProperty("Load the next block of records","Загрузить следующий блок строк");
    dictionary.setProperty("Load the last block of records","Загрузить последний блок строк");

    dictionary.setProperty("Insert","Вставить");
    dictionary.setProperty("Edit","Редактировать");
    dictionary.setProperty("Copy","Копировать");
    dictionary.setProperty("Delete","Удалить");
    dictionary.setProperty("Save","Сохранить");
    dictionary.setProperty("Reload","Перезагрузить");
    dictionary.setProperty("Export","Экспорт");
    dictionary.setProperty("Filter","Фильтр");

    // MDI Frame...
    dictionary.setProperty("file","Файл");
    dictionary.setProperty("exit","Выход");
    dictionary.setProperty("filemnemonic","F");
    dictionary.setProperty("exitmnemonic","E");
    dictionary.setProperty("change user","Сменить пользователя");
    dictionary.setProperty("changeusermnemonic","U");
    dictionary.setProperty("changelanguagemnemonic","L");
    dictionary.setProperty("help","Помощь");
    dictionary.setProperty("about","О программе");
    dictionary.setProperty("helpmnemonic","H");
    dictionary.setProperty("aboutmnemonic","A");
    dictionary.setProperty("are you sure to quit application?","Вы действительно хотите закрыть приложение?");
    dictionary.setProperty("quit application","Закрытие приложения");
    dictionary.setProperty("forcegcmnemonic","F");
    dictionary.setProperty("Force GC","Принудительная очистка");
    dictionary.setProperty("Java Heap","Java Heap");
    dictionary.setProperty("used","использовано");
    dictionary.setProperty("allocated","выделено");
    dictionary.setProperty("change language","Сменить язык");
    dictionary.setProperty("changemnemonic","L");
    dictionary.setProperty("cancelmnemonic","C");
    dictionary.setProperty("cancel","Отмена");
    dictionary.setProperty("yes","Да");
    dictionary.setProperty("no","Нет");
    dictionary.setProperty("Functions","Формы");
    dictionary.setProperty("Error while executing function","Ошибка при открытии окна");
    dictionary.setProperty("Error","Ошибка");
    dictionary.setProperty("infoPanel","Помощь");
    dictionary.setProperty("imageButton","О программе");
    dictionary.setProperty("Window","Окно");
    dictionary.setProperty("windowmnemonic","W");
    dictionary.setProperty("Close All","Закрыть все");
    dictionary.setProperty("closeallmnemonic","A");
    dictionary.setProperty("closemnemonic","C");
    dictionary.setProperty("Press ENTER to find function","Нажмите ENTER для поиска формы");
    dictionary.setProperty("Find Function","Поиск формы");
    dictionary.setProperty("Operation in progress...","Задача выполняется...");
    dictionary.setProperty("close window","Закрытие окна");
    dictionary.setProperty("reduce to icon","Свернуть");
    dictionary.setProperty("save changes?", "Сохранить изменения?");
    dictionary.setProperty("confirm window closing","Подтверждение закрытия окна");
    dictionary.setProperty("change background","Сменить фон");
    dictionary.setProperty("reset background","Отменить фон");

    dictionary.setProperty("switch","Переключить");
    dictionary.setProperty("switchmnemonic","S");
    dictionary.setProperty("window name","Имя окна");
    dictionary.setProperty("opened windows","Открытые окна");
    dictionary.setProperty("tile horizontally","Расположить по горизонтали");
    dictionary.setProperty("tilehorizontallymnemonic","H");
    dictionary.setProperty("tile vertically","Расположить по вертикали");
    dictionary.setProperty("tileverticallymnemonic","V");
    dictionary.setProperty("cascade","Каскадом");
    dictionary.setProperty("cascademnemonic","C");
    dictionary.setProperty("minimize","Свернуть");
    dictionary.setProperty("minimizemnemonic","M");
    dictionary.setProperty("minimize all","Свернуть все");
    dictionary.setProperty("minimizeallmnemonic","A");
    dictionary.setProperty("selected frame","выбранный фрейм");

    // server...
    dictionary.setProperty("Client request not supported","Запрос клиента не выполнен");
    dictionary.setProperty("User disconnected","Пользователь отключен");
    dictionary.setProperty("Updating not performed: the record was previously updated.","Обновление не выполнено: строка ранее была изменена другим пользователем.");

    // wizard...
    dictionary.setProperty("back","Назад");
    dictionary.setProperty("next","Далее");
    dictionary.setProperty("finish","Финиш");

    // image panel...
    dictionary.setProperty("image selection","Выбор фотографии");

    // tip of the day panel...
    dictionary.setProperty("show 'tip of the day' after launching","Показать 'Совет дня' после запуска");
    dictionary.setProperty("previous tip","Предыдущий совет");
    dictionary.setProperty("next tip","Следующий совет");
    dictionary.setProperty("close","Закрыть");
    dictionary.setProperty("tip of the day","Совет дня");
    dictionary.setProperty("select tip","Выбрать совет");
    dictionary.setProperty("tip name","Наименование совета");
    dictionary.setProperty("tips list","Список советов");

    // progress panel...
    dictionary.setProperty("progress","Прогресс");

    // licence agreement...
    dictionary.setProperty("i accept the terms in the licence agreement","Я принимаю условия лицензионного соглашения");
    dictionary.setProperty("ok","Принять");
    dictionary.setProperty("i do not accept the terms in the licence agreement","Я не принимаю условия лицензионного соглашения");

    // property grid control
    dictionary.setProperty("property name","Имя");
    dictionary.setProperty("property value","Значение");

    // grid profile
    dictionary.setProperty("grid profile management","Управление профилем таблицы");
    dictionary.setProperty("restore default grid profile","Восстановить стандартный профиль таблицы");
    dictionary.setProperty("create new grid profile","Создать новый профиль таблицы");
    dictionary.setProperty("profile description","Наименование профиля");
    dictionary.setProperty("remove current grid profile","Удалить текущий профиль таблицы");
    dictionary.setProperty("select grid profile","Выбрать профиль таблицы");
    dictionary.setProperty("default profile","Стандартный пролфиль");

    // search box
    dictionary.setProperty("search","Поиск");
    dictionary.setProperty("not found","Не найдено");

    // drag...
    dictionary.setProperty("drag","Перетащить");

    // pivot table...
    dictionary.setProperty("pivot table settings","Настройка сводной таблицы");
    dictionary.setProperty("row fields","Строка полей");
    dictionary.setProperty("column fields","Колонка полей");
    dictionary.setProperty("data fields","Поля данных");
    dictionary.setProperty("filtering conditions","Условия фильтрации");
    dictionary.setProperty("field","Поле");
    dictionary.setProperty("checked","Проверено");
    dictionary.setProperty("at least one data field must be selected","По крайней мере одно поле данных должно быть выбрано.");
    dictionary.setProperty("at least one row field must be selected","По крайней мере одна строка поля должна быть выбрана.");
    dictionary.setProperty("at least one column field must be selected","По крайней мере один столбец поля должен быть выбран.");
    dictionary.setProperty("expand all","Развернуть все");
    dictionary.setProperty("collapse all","Свернуть все");

    dictionary.setProperty(Consts.EQ,"Равно");
    dictionary.setProperty(Consts.GE,"Больше или равно");
    dictionary.setProperty(Consts.GT,"Больше");
    dictionary.setProperty(Consts.IS_NOT_NULL,"Не пустое");
    dictionary.setProperty(Consts.IS_NULL,"Пустое");
    dictionary.setProperty(Consts.LE,"Меньше или равно");
    dictionary.setProperty(Consts.LIKE,"Содержит");
    dictionary.setProperty(Consts.LT,"Меньше");
    dictionary.setProperty(Consts.NEQ,"Не равно");
    dictionary.setProperty(Consts.IN,"Содержит значения");
    dictionary.setProperty(Consts.ASC_SORTED,"По возрастанию");
    dictionary.setProperty(Consts.DESC_SORTED,"По убыванию");
    dictionary.setProperty(Consts.NOT_IN,"Не содержит значения");


    resources = new Resources(
      dictionary,
      currencySymbol,
      ',',
      ' ',
      Resources.YMD,
      true,
      dateFormatSeparator,
      "HH:mm",
      "RU",
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
