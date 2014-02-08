package org.openjump.core.ui.io.file;

public class Option {

    private String name;
    private String type;
    private boolean required;
    private Object defaultValue = null;

    public Option(final String name, final String type, final boolean required) {
        super();
        this.name = name;
        this.type = type;
        this.required = required;
    }

    public Option(final String name, final String type, final Object defaultValue, final boolean required) {
        super();
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getDefault() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    /**
     * Overwrite equals(), because the Object.equals works wrong for an Option
     * instance. So we must compare each single value. This is especially
     * important if we have some Options stored in a List and do a
     * List.remove().
     *
     * @param obj
     * @return true if both objects are equal.
     */
    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj instanceof Option) {
            Option option = (Option) obj;
            equal = this.name.equals(option.getName()) && this.type.equals(option.getType()) && this.required == option.isRequired() && this.defaultValue == option.defaultValue;
        }
        return equal;
    }
}
