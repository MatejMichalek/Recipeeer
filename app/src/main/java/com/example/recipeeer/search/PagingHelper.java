package com.example.recipeeer.search;

public class PagingHelper {
    // max no of results received on search request
    public static final int noOfRecipesPerPage = 16;

    // total no of recipes for given search term
    private int totalNoOfRecipes;
    // current offset for results
    private int offset;

    public PagingHelper(int totalNoOfRecipes, int offset) {
        this.totalNoOfRecipes = totalNoOfRecipes;
        this.offset = offset;
    }

    // calculate desired offset for new request
    public int getDesiredOffset(int pageChange) {
        return offset + (pageChange*noOfRecipesPerPage);
    }

    // checks if previous page can be requested
    public boolean canGoToPreviousPage() {
        return offset>=noOfRecipesPerPage;
    }

    // checks if next page can be requested
    public boolean canGoToNextPage() {
        return offset+noOfRecipesPerPage<totalNoOfRecipes;
    }
}
