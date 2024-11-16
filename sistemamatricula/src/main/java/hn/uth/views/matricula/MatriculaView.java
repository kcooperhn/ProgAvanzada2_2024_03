package hn.uth.views.matricula;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Position;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import hn.uth.controller.MatriculaInteractor;
import hn.uth.controller.MatriculaInteractorImpl;
import hn.uth.data.Alumno;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@PageTitle("Matricula")
@Route("checkout-form")
@Menu(order = 2, icon = "line-awesome/svg/credit-card.svg")
public class MatriculaView extends Div implements MatriculaViewModel {

    private static final Set<String> states = new LinkedHashSet<>();
    private static final Set<String> countries = new LinkedHashSet<>();
    private ComboBox<Alumno> alumno;
    private List<Alumno> alumnos;
    
    private MatriculaInteractor controlador;

    public MatriculaView() {
        addClassNames("matricula-view");
        addClassNames(Display.FLEX, FlexDirection.COLUMN, Height.FULL);
        
        controlador = new MatriculaInteractorImpl(this);
        alumnos = new ArrayList<>();

        Main content = new Main();
        content.addClassNames(Display.GRID, Gap.XLARGE, AlignItems.START, JustifyContent.CENTER, MaxWidth.SCREEN_MEDIUM,
                Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        content.add(createCheckoutForm());
        content.add(createAside());
        add(content);
        
        controlador.consultarAlumnos();
    }

    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames(Display.FLEX, FlexDirection.COLUMN, Flex.GROW);

        H2 header = new H2("Ficha de Matricula");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph note = new Paragraph("Ingrese los datos del alumno y las asignaturas a registrar.");
        note.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        checkoutForm.add(header, note);

        checkoutForm.add(createPersonalDetailsSection());
        checkoutForm.add(createShippingAddressSection());
        checkoutForm.add(createPaymentInformationSection());
        checkoutForm.add(new Hr());
        checkoutForm.add(createFooter());

        return checkoutForm;
    }

    private Section createPersonalDetailsSection() {
        Section personalDetails = new Section();
        personalDetails.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);

        Paragraph stepOne = new Paragraph("Matricula 1/3");
        stepOne.addClassNames(Margin.NONE, FontSize.SMALL, TextColor.SECONDARY);

        H3 header = new H3("Información del Alumno");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL, FontSize.XXLARGE);
        
        alumno = new ComboBox<>("Alumno");
        alumno.setAllowCustomValue(true);

        EmailField email = new EmailField("Correo Electrónico");
        email.setReadOnly(true);
        email.addClassNames(Margin.Bottom.SMALL);

        TextField phone = new TextField("Número de teléfono");
        phone.setReadOnly(true);
        phone.addClassNames(Margin.Bottom.SMALL);

        personalDetails.add(stepOne, header, alumno, email, phone);
        return personalDetails;
    }

    private Section createShippingAddressSection() {
        Section shippingDetails = new Section();
        shippingDetails.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);

        Paragraph stepTwo = new Paragraph("Matricula 2/3");
        stepTwo.addClassNames(Margin.NONE, FontSize.SMALL, TextColor.SECONDARY);

        H3 header = new H3("Asignatura a Matricular");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL, FontSize.XXLARGE);

        ComboBox<String> asignaturaSeleccionada = new ComboBox<>("Asignatura");
        asignaturaSeleccionada.setRequiredIndicatorVisible(true);
        asignaturaSeleccionada.addClassNames(Margin.Bottom.SMALL);

        TextField catedratico = new TextField("Catedrático");
        catedratico.setReadOnly(true);
        catedratico.addClassNames(Margin.Bottom.SMALL);

        Div subSection = new Div();
        subSection.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);

        TextField modalidad = new TextField("Modalidad");
        modalidad.setReadOnly(true);
        modalidad.addClassNames(Margin.Bottom.SMALL);

        TextField horario = new TextField("Horario");
        horario.setReadOnly(true);
        horario.addClassNames(Flex.GROW, Margin.Bottom.SMALL);
        
        TextField precio = new TextField("Valor de la Asignatura");
        precio.setReadOnly(true);
        precio.addClassNames(Margin.Bottom.SMALL);

        subSection.add(modalidad, horario);

        Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
        plusButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        plusButton.setAriaLabel("Agregar Clase");

        shippingDetails.add(stepTwo, header, asignaturaSeleccionada, catedratico, subSection, precio, plusButton);
        return shippingDetails;
    }

    private Component createPaymentInformationSection() {
        Section paymentInfo = new Section();
        paymentInfo.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);

        Paragraph stepThree = new Paragraph("Matricula 3/3");
        stepThree.addClassNames(Margin.NONE, FontSize.SMALL, TextColor.SECONDARY);

        H3 header = new H3("Medio de Pago");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL, FontSize.XXLARGE);

        TextField cardHolder = new TextField("Nombre de Tarjeta de Crédito");
        cardHolder.setRequiredIndicatorVisible(true);
        cardHolder.setPattern("[\\p{L} \\-]+");
        cardHolder.addClassNames(Margin.Bottom.SMALL);

        Div subSectionOne = new Div();
        subSectionOne.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);

        TextField cardNumber = new TextField("Número de Tarjeta");
        cardNumber.setRequiredIndicatorVisible(true);
        cardNumber.setPattern("[\\d ]{12,23}");
        cardNumber.addClassNames(Margin.Bottom.SMALL);

        TextField securityCode = new TextField("CVC");
        securityCode.setRequiredIndicatorVisible(true);
        securityCode.setPattern("[0-9]{3,4}");
        securityCode.addClassNames(Flex.GROW, Margin.Bottom.SMALL);
        securityCode.setHelperText("¿Qué es esto?");

        subSectionOne.add(cardNumber, securityCode);

        Div subSectionTwo = new Div();
        subSectionTwo.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);

        Select<String> expirationMonth = new Select<>();
        expirationMonth.setLabel("Mes de Expiración");
        expirationMonth.setRequiredIndicatorVisible(true);
        expirationMonth.setItems("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        Select<String> expirationYear = new Select<>();
        expirationYear.setLabel("Año de Expiración");
        expirationYear.setRequiredIndicatorVisible(true);
        expirationYear.setItems("22", "23", "24", "25", "26");

        subSectionTwo.add(expirationMonth, expirationYear);

        paymentInfo.add(stepThree, header, cardHolder, subSectionTwo);
        return paymentInfo;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Vertical.MEDIUM);

        Button cancel = new Button("Cancelar Matricula");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button pay = new Button("Pagar Matricula", new Icon(VaadinIcon.LOCK));
        pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        footer.add(cancel, pay);
        return footer;
    }

    private Aside createAside() {
        Aside aside = new Aside();
        aside.addClassNames(Background.CONTRAST_5, BoxSizing.BORDER, Padding.LARGE, BorderRadius.LARGE,
                Position.STICKY);
        Header headerSection = new Header();
        headerSection.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Bottom.MEDIUM);
        H3 header = new H3("Prematricula");
        header.addClassNames(Margin.NONE);
        Button edit = new Button("Editar");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header, edit);

        UnorderedList ul = new UnorderedList();
        ul.addClassNames(ListStyleType.NONE, Margin.NONE, Padding.NONE, Display.FLEX, FlexDirection.COLUMN, Gap.MEDIUM);

        ul.add(createListItem("Programación Estructurada", "L-J 6:10pm", "L. 1,000.00"));
        ul.add(createListItem("Vectores y Matrices", "L-J 7:20pm", "L. 1,000.00"));
        ul.add(createListItem("Inteligencia Artificial", "L-J 8:10pm", "L. 1,200.00"));

        aside.add(headerSection, ul);
        return aside;
    }

    private ListItem createListItem(String primary, String secondary, String price) {
        ListItem item = new ListItem();
        item.addClassNames(Display.FLEX, JustifyContent.BETWEEN);

        Div subSection = new Div();
        subSection.addClassNames(Display.FLEX, FlexDirection.COLUMN);

        subSection.add(new Span(primary));
        Span secondarySpan = new Span(secondary);
        secondarySpan.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subSection.add(secondarySpan);

        Span priceSpan = new Span(price);

        item.add(subSection, priceSpan);
        return item;
    }

	@Override
	public void mostrarAlumnosEnComboBox(List<Alumno> items) {
		Collection<Alumno> itemsCollection = items;
		this.alumnos = items;
		alumno.setItems(itemsCollection);
		alumno.setItemLabelGenerator(
		        person -> person.getNombre() + " " + person.getApellido());
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
}
