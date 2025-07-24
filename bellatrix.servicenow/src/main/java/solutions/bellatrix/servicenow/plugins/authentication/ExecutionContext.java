package solutions.bellatrix.servicenow.plugins.authentication;

import solutions.bellatrix.servicenow.snSetupData.enums.ServiceNowUser;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExecutionContext {
    private static ThreadLocal<Map<String, ServiceNowUser>> currentUser;
    private static ThreadLocal<Map<ServiceNowUser, Map<String, String>>> cookiesPerUser;

    static {
        currentUser = new ThreadLocal<>();
        currentUser.set(new HashMap<>());
        cookiesPerUser = new ThreadLocal<>();
        cookiesPerUser.set(new HashMap<>());
    }

    public static ServiceNowUser getUser(Class<?> classType) {
        if (currentUser.get().containsKey(classType.getName())) {
            return currentUser.get().get(classType.getName());
        }

        return ServiceNowUser.NONE;
    }

    public static void setUser(Class<?> classType, ServiceNowUser user) {
        currentUser.get().put(classType.getName(), user);
    }

    public static void addCookieEntry(ServiceNowUser user, String cookieKey, String cookieValue) {
        if (!cookiesPerUser.get().containsKey(user)) {
            cookiesPerUser.get().put(user, new HashMap<>());
        }

        if (!cookiesPerUser.get().get(user).containsKey(cookieKey)) {
            cookiesPerUser.get().get(user).put(cookieKey, cookieValue);
        }
    }

    public static Map<String, String> getCookiesForUser(ServiceNowUser user) {
        if (cookiesPerUser.get().containsKey(user)) {
            return cookiesPerUser.get().get(user);
        }

        return Collections.emptyMap();
    }
}