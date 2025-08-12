package solutions.bellatrix.servicenow.utilities.setupData;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import solutions.bellatrix.servicenow.infrastructure.core.entities.ServiceNowEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.models.annotations.TableTarget;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@TableTarget("fx_currency2_instance")
public class FxCurrency2Instance extends ServiceNowEntity<FxCurrency2Instance> {
    @SerializedName("amount")
    private BigDecimal amount;
    @SerializedName("currency")
    private String currency;

    /**
     * Example: "$2,000.00"
     */
    public String getAmountUsdFormatted() {
        var formatter = NumberFormat.getCurrencyInstance(Locale.US);
        var result = formatter.format(amount);

        return result;
    }

    /**
     * Example: "USD;2000"
     */
    public String toStringForTableApi() {
        return String.format("%s;%s", this.getCurrency(), this.getAmount());
    }

    public String toString() {
        return this.amount.toString();
    }
}