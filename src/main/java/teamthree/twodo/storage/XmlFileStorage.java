package teamthree.twodo.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.commons.util.XmlUtil;

/**
 * Stores taskList data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given taskList data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableTaskList taskList) throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, taskList);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * For use with task lists. Main intended use is to store notified tasks
     * list
     */
    public static void saveNotificationToFile(File file, List<XmlAdaptedTask> taskList) throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, taskList);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns TaskList in the file or an empty TaskList
     */
    public static XmlSerializableTaskList loadDataFromSaveFile(File file)
            throws DataConversionException, FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableTaskList.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
