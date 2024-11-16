package hn.uth.views.alumnos;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import hn.uth.controller.AlumnosInteractor;
import hn.uth.controller.AlumnosInteractorImpl;
import hn.uth.data.Alumno;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.util.Collection;

@PageTitle("Alumnos")
@Route("/:alumnoID?/:action?(edit)")
@Menu(order = 0, icon = "line-awesome/svg/user-alt-solid.svg")
@RouteAlias("")
public class AlumnosView extends Div implements BeforeEnterObserver, AlumnosViewModel {

    private final String ALUMNO_ID = "alumnoID";
    private final String ALUMNO_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<Alumno> grid = new Grid<>(Alumno.class, false);

    private TextField nombre;
    private TextField apellido;
    private TextField correo;
    private TextField telefono;
    private DatePicker fechaNacimiento;
    private TextField carrera;

    private final Button cancel = new Button("Cancelar", new Icon(VaadinIcon.CLOSE_SMALL));
    private final Button save = new Button("Guardar", new Icon(VaadinIcon.CHECK));

    //private final BeanValidationBinder<Alumno> binder;

    private Alumno alumno;
    private List<Alumno> alumnos;
    private AlumnosInteractor controlador;


    public AlumnosView() {
        addClassNames("alumnos-view");
        
        controlador = new AlumnosInteractorImpl(this);
        alumnos = new ArrayList<>();

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("apellido").setAutoWidth(true);
        grid.addColumn("correo").setAutoWidth(true);
        grid.addColumn("telefono").setAutoWidth(true);
        grid.addColumn("fecha_nacimiento").setAutoWidth(true).setHeader("Fecha de Nacimiento");
        grid.addColumn("carrera").setAutoWidth(true);
        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ALUMNO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(AlumnosView.class);
            }
        });

        // Configure Form
        //binder = new BeanValidationBinder<>(Alumno.class);

        // Bind fields. This is where you'd define e.g. validation rules

        //binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.alumno == null) {
                	//ENTRA AQUI SI EL ALUMNO ES NUEVO
                    this.alumno = new Alumno();
                    this.alumno.setNombre(nombre.getValue());
                    this.alumno.setApellido(apellido.getValue());
                    this.alumno.setCorreo(correo.getValue());
                    this.alumno.setTelefono(telefono.getValue());
                    this.alumno.setFecha_nacimiento(fechaNacimiento.getValue());
                    this.alumno.setCarrera(carrera.getValue());
                    
                    this.controlador.crearAlumno(alumno);
                }else {
                	//ENTRA AQUI SI EL ALUMNO YA EXISTIA
                	this.alumno.setNombre(nombre.getValue());
                    this.alumno.setApellido(apellido.getValue());
                    this.alumno.setCorreo(correo.getValue());
                    this.alumno.setTelefono(telefono.getValue());
                    this.alumno.setFecha_nacimiento(fechaNacimiento.getValue());
                    this.alumno.setCarrera(carrera.getValue());
                    
                    this.controlador.actualizarAlumno(alumno);
                	
                }
                clearForm();
                refreshGrid();
                UI.getCurrent().navigate(AlumnosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        controlador.consultarAlumnos();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> alumnoId = event.getRouteParameters().get(ALUMNO_ID).map(Long::parseLong);
        if (alumnoId.isPresent()) {
        	Alumno alumnoSeleccionado = obtenerAlumno(alumnoId.get());
        	if(alumnoSeleccionado == null) {
        		mostrarMensajeError("El alumno con el id "+alumnoId.get()+" no existe");
           		refreshGrid(); 
        		event.forwardTo(AlumnosView.class);
        	}else {
        		populateForm(alumnoSeleccionado);
        	}
        }
    }

    private Alumno obtenerAlumno(Long id) {
		Alumno encotrado = null;
		for(Alumno al: alumnos) {
			if(al.getId() == id) {
				encotrado = al;
				break;
			}
		}
		return encotrado;
	}

	private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        nombre = new TextField("Nombre");
        nombre.setClearButtonVisible(true);
        nombre.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        apellido = new TextField("Apellido");
        apellido.setClearButtonVisible(true);
        apellido.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        correo = new TextField("Correo");
        correo.setClearButtonVisible(true);
        correo.setPrefixComponent(VaadinIcon.MAILBOX.create());
        
        telefono = new TextField("Telefono");
        telefono.setClearButtonVisible(true);
        telefono.setPrefixComponent(VaadinIcon.PHONE.create());
        telefono.setHelperText("Formato: +(504)9988-7766");
        
        fechaNacimiento = new DatePicker("Fecha Nacimiento");
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        fechaNacimiento.setMin(now.plusYears(-50));
        fechaNacimiento.setMax(now.plusDays(-60));
        
        
        
        
        carrera = new TextField("Carrera");
        formLayout.add(nombre, apellido, correo, telefono, fechaNacimiento, carrera);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        
        
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        
        
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
        this.controlador.consultarAlumnos();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Alumno value) {
        this.alumno = value;
        if(value == null) {
        	nombre.setValue("");
            apellido.setValue("");
            correo.setValue("");
            telefono.setValue("");
            fechaNacimiento.clear();
            carrera.setValue("");
        }else {
        	nombre.setValue(value.getNombre());
            apellido.setValue(value.getApellido());
            correo.setValue(value.getCorreo());
            telefono.setValue(value.getTelefono());
            LocalDate fechaNac = value.getFecha_nacimiento().toLocalDate();
			
			fechaNacimiento.setValue(fechaNac);
            carrera.setValue(value.getCarrera());
        }
    }

	@Override
	public void mostrarAlumnosEnGrid(List<Alumno> items) {
		Collection<Alumno> itemsCollection = items;
		this.alumnos = items;
		grid.setItems(itemsCollection);
	}

	@Override
	public void mostrarMensajeError(String mensaje) {
		Notification notification = new Notification();
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

		Div text = new Div(new Text(mensaje));

		Button closeButton = new Button(new Icon("lumo", "cross"));
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		closeButton.setAriaLabel("Close");
		closeButton.addClickListener(event -> {
		    notification.close();
		});

		HorizontalLayout layout = new HorizontalLayout(text, closeButton);
		layout.setAlignItems(Alignment.CENTER);

		notification.add(layout);
		notification.open();
	}

	@Override
	public void mostrarMensajeExito(String mensaje) {
		Notification notification = Notification.show(mensaje);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		notification.open();
	}
}
