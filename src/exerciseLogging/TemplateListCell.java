package exerciseLogging;

import javafx.scene.control.ListCell;

/**
 * Created by vegard on 14.03.2016.
 */
public class TemplateListCell extends ListCell<Template> {

    private Template template;

    public TemplateListCell(Template template) {
        this.template = template;
    }

    public int getTemplateId() {
        return template.getId();
    }

    @Override
    public void updateItem(Template item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(template.getName());
        }
    }
}
