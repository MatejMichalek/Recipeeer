package com.example.recipeeer.search;

public class PagingHelper {
    public static final int noOfRecipesPerPage = 16;

    private int totalNoOfRecipes;
    private int offset;

    public PagingHelper(int totalNoOfRecipes, int offset) {
        this.totalNoOfRecipes = totalNoOfRecipes;
        this.offset = offset;
    }

    public int getTotalNoOfRecipes() {
        return totalNoOfRecipes;
    }

    public int getPageNo() {
        return (offset/noOfRecipesPerPage)+1;
    }

    public int getDesiredOffset(int pageChange) {
        return offset + (pageChange*noOfRecipesPerPage);
    }

    public boolean canGoToPreviousPage() {
        return offset>=noOfRecipesPerPage;
    }

    public boolean canGoToNextPage() {
        return offset+noOfRecipesPerPage<totalNoOfRecipes;
    }
}
