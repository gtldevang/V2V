package model.product;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import controller.UtilController;

import viewmodel.ProductViewModel;

public class ProductBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public ProductBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(FindProductBackingForm.class, ProductBackingForm.class, ProductViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    ProductBackingForm form = (ProductBackingForm) obj;

    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateStringValid(createdOn))
      errors.rejectValue("product.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());

    String expiresOn = form.getExpiresOn();
    if (!CustomDateFormatter.isDateStringValid(expiresOn))
      errors.rejectValue("product.expiresOn", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());

    utilController.checkRequiredFields(form, "product", errors);
  }
}