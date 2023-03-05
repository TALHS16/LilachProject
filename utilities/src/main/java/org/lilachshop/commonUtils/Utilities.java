package org.lilachshop.commonUtils;

import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Utilities {
    /**
     * Converts Object to it's type.
     * Note: Any type of list will be represented as a generic list... try it out.
     *
     * @param o Object to get it's type
     * @return String representing it's type
     */
    static public String getClassType(Object o) {
        String type = o.getClass().getTypeName();
        String sub_type = "";
        if (type.contains("List")) {
            try {
                var contained_object = ((List<?>) o).get(0);
                sub_type = contained_object.getClass().getSimpleName();
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
            type = "List<" + sub_type + ">";
        } else {
            String[] full_subtype = type.split("\\.");
            type = full_subtype[full_subtype.length - 1];
        }
        return type;
    }

    public static <T> void setTextFieldFactory(TableColumn<T, String> col, BiFunction<T, String, Void> fieldSetter, Function<T, String> fieldGetter, Consumer<T> dbUpdater, Boolean... uniqueValSet) {
        ButtonType hebrewYes = new ButtonType("כן", ButtonBar.ButtonData.OK_DONE);
        ButtonType hebrewNo = new ButtonType("לא", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "פעולה זו אינה הפיכה", hebrewYes, hebrewNo);
        alert.setTitle("עדכון שדה");
        alert.setHeaderText("האם ברצונך לעדכן שדה זה?");
        alert.setResizable(false);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        col.setCellFactory(TextFieldTableCell.forTableColumn());
        col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<T, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<T, String> event) {
                T row_item = (T) event.getTableView().getItems().get(event.getTablePosition().getRow());

                Optional<ButtonType> result = alert.showAndWait();
                ButtonType buttonType = result.orElse(hebrewNo);
                if (buttonType == hebrewYes) {
                    if (uniqueValSet.length < 1 || uniqueValSet[0].equals(Boolean.FALSE)) {
                        fieldSetter.apply(row_item, event.getNewValue());
                        dbUpdater.accept(row_item);
                    } else {
                        // check uniqueness, if true should set value, else show error message.
                        List<T> items = event.getTableView().getItems();
                        boolean isUnique = true;
                        for (T item : items) {
                            if (Objects.equals(event.getNewValue(), fieldGetter.apply(item))) {
                                isUnique = false;
                                break;
                            }
                        }
                        if (isUnique) {
                            fieldSetter.apply(row_item, event.getNewValue());
                            dbUpdater.accept(row_item);
                        } else {
                            Alert notUniqueAlert = new Alert(Alert.AlertType.ERROR, "פעולה בוטלה");
                            notUniqueAlert.setHeaderText("לא ניתן לשנות לערך קיים.");
                            notUniqueAlert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                            notUniqueAlert.show();
                        }
                    }
                }
                col.getTableView().refresh();
            }
        });
    }

    public static byte[] imgFileToBytesConverter(File imageFile, String extension) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, extension, out);
        return out.toByteArray();
    }

    public static Image bytesToImageConverter(byte[] imgBytes) {
        return new Image(new ByteArrayInputStream(imgBytes));
    }

    public static Boolean hasTheSameDate(LocalDateTime date, LocalDateTime orderDate) {
        if (date.getDayOfMonth() == orderDate.getDayOfMonth() &&
                date.getYear() == orderDate.getYear() &&
                date.getMonth() == orderDate.getMonth()) {
            return true;
        }
        return false;
    }

    public static boolean containHebrew(String str) {
        str = str.replaceAll("[/.,\\-]", "");
        str = str.replaceAll(" ","");
        boolean valid = str.chars().allMatch(p -> ((p <= 0x05ea && p >= 0x05d0) || (p == ' ') || (p == '-') || (p == ',')));
        System.out.println(valid);
        return valid;
    }

    public static boolean containHebrewOrNumber(String str) {
        str = str.replaceAll("[/.,\\-]","");
        str = str.replaceAll("\\d", "");
        str = str.replaceAll(" ","");
        boolean valid = str.chars().allMatch(p -> ((p <= 0x05ea && p >= 0x05d0) || (p == ' ') || (p == '-')));
        System.out.println(valid);
        return valid;
    }
}
