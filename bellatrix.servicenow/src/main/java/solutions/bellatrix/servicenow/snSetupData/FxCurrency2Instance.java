package solutions.bellatrix.servicenow.snSetupData;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FxCurrency2Instance extends ApiEntity {
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