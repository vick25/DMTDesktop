package com.osfac.dmt.workbench.model;

/**
 * An addition, removal, or modification of a Category.
 *
 * @see Category
 * @see CategoryEventType
 */
public class CategoryEvent {

    private Category category;
    private CategoryEventType type;
    private int categoryIndex;

    public CategoryEvent(Category category, CategoryEventType type,
            int categoryIndex) {
        this.category = category;
        this.type = type;
        this.categoryIndex = categoryIndex;
    }

    public CategoryEventType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }
}
