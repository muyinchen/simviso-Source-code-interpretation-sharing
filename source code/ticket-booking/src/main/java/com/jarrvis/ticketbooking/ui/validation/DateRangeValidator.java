package com.jarrvis.ticketbooking.ui.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;



@Slf4j
public class DateRangeValidator implements ConstraintValidator<DateRange, Object>
{

	private DateRange dateRange;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(final DateRange dateRange)
	{
		this.dateRange = dateRange;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(final Object source, final ConstraintValidatorContext constraintValidatorContext)
	{

		final Class<? extends Object> beanClass = source.getClass();
		final Field startDateFieldValue = ReflectionUtils.findField(beanClass, dateRange.startDateFieldName());
		final Field endDateFieldValue = ReflectionUtils.findField(beanClass, dateRange.endDateFieldName());
		if (startDateFieldValue != null && endDateFieldValue != null)
		{
			startDateFieldValue.setAccessible(true);
			endDateFieldValue.setAccessible(true);

			try
			{
				final LocalDateTime startDate = (LocalDateTime) startDateFieldValue.get(source);
				final LocalDateTime endDate = (LocalDateTime) endDateFieldValue.get(source);
				return startDate == null || endDate == null || !endDate.isBefore(startDate);
			}
			catch (IllegalArgumentException | IllegalAccessException e1)
			{
				log.debug(e1.getMessage(), e1);
			}
		}
		return false;
	}

}
