package com.xenonsoft.client.acme.vaadin.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import com.xenonsoft.client.acme.model.Supplier;
import com.xenonsoft.client.acme.model.SupplierRepository;


@SpringComponent
@UIScope
public class SupplierDataEditor extends VerticalLayout implements KeyNotifier {

    private final SupplierRepository repository;

    private Supplier employee;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Supplier> binder = new Binder<>(Supplier.class);
    private ChangeHandler changeHandler;

    @Autowired
    public SupplierDataEditor(SupplierRepository repository) {
        this.repository = repository;

        add(firstName, lastName, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmployee(employee));
        setVisible(false);
    }

    void delete() {
        repository.delete(employee);
        changeHandler.onChange();
    }

    void save() {
        repository.save(employee);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editEmployee(Supplier employee) {
        if (employee == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = employee.getId() != null;
        if (persisted) {
            this.employee = repository.findById(employee.getId()).get();
        } else {
            this.employee = employee;
        }

        cancel.setVisible(persisted);
        binder.setBean(this.employee);
        setVisible(true);
        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }
}
