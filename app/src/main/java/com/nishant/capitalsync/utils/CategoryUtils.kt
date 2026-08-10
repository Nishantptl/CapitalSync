package com.nishant.capitalsync.utils

import androidx.compose.ui.graphics.Color
import com.nishant.capitalsync.ui.theme.*
import android.content.Context
import com.nishant.capitalsync.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Category(
    val id: String,
    val name: String,
    val icon: String,
    @SerialName("sub_categories")
    val subCategories: List<String>
)

@Serializable
data class CategoriesData(
    @SerialName("expense_categories")
    val expenseCategories: List<Category>,
    @SerialName("income_categories")
    val incomeCategories: List<Category>
)

object CategoryUtils {
    private var categoriesData: CategoriesData? = null

    fun loadCategories(context: Context) {
        if (categoriesData != null) return
        try {
            context.assets.open("categories.json").bufferedReader().use { reader ->
                categoriesData = Json.decodeFromString<CategoriesData>(reader.readText())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val expenseCategories: List<Category>
        get() = categoriesData?.expenseCategories ?: emptyList()

    val incomeCategories: List<Category>
        get() = categoriesData?.incomeCategories ?: emptyList()

    fun getCategoryIcon(nameOrIcon: String): Int {
        // Direct category name mapping
        return when (nameOrIcon.lowercase()) {
            "food & dining" -> R.drawable.fork_spoon_24px
            "transportation" -> R.drawable.moped_24px
            "housing & utilities" -> R.drawable.home_24px
            "shopping" -> R.drawable.shopping_cart_24px
            "entertainment" -> R.drawable.stadia_controller_24px
            "health & fitness" -> R.drawable.health_and_safety_24px
            "others" -> R.drawable.category_24px
            "active income" -> R.drawable.payments_24px
            "passive income" -> R.drawable.trending_up_24px
            "balance adjustment" -> R.drawable.construction_24px
            else -> {
                // Try direct icon name match
                val iconRes = getIconResByName(nameOrIcon)
                if (iconRes != R.drawable.construction_24px) return iconRes

                // Try matching by category name or sub-category name
                val allCategories = expenseCategories + incomeCategories
                val mainCat = allCategories.find { it.name.equals(nameOrIcon, ignoreCase = true) }
                if (mainCat != null) return getIconResByName(mainCat.icon)

                val parentCat = allCategories.find { parent ->
                    parent.subCategories.any { sub -> sub.equals(nameOrIcon, ignoreCase = true) }
                }
                if (parentCat != null) return getIconResByName(parentCat.icon)

                R.drawable.category_24px
            }
        }
    }

    fun getMainCategoryForSub(subCategory: String): String {
        val allCategories = expenseCategories + incomeCategories
        // If it's already a main category name, return it
        val mainCat = allCategories.find { it.name.equals(subCategory, ignoreCase = true) }
        if (mainCat != null) return mainCat.name

        // If it's a sub-category, find and return its parent main category name
        val parentCat = allCategories.find { parent ->
            parent.subCategories.any { sub -> sub.equals(subCategory, ignoreCase = true) }
        }
        return parentCat?.name ?: subCategory
    }

    private fun getIconResByName(iconName: String): Int {
        return when (iconName) {
            "restaurant", "fork_spoon_24px" -> R.drawable.fork_spoon_24px
            "directions_car", "moped_24px" -> R.drawable.moped_24px
            "home", "home_24px" -> R.drawable.home_24px
            "shopping_bag", "shopping_cart_24px" -> R.drawable.shopping_cart_24px
            "stadia_controller", "stadia_controller_24px" -> R.drawable.stadia_controller_24px
            "health_and_safety", "health_and_safety_24px" -> R.drawable.health_and_safety_24px
            "category", "category_24px", "others" -> R.drawable.category_24px
            "payments", "payments_24px" -> R.drawable.payments_24px
            "trending_up", "trending_up_24px" -> R.drawable.trending_up_24px
            "account_balance_wallet", "account_balance_wallet_24px" -> R.drawable.account_balance_wallet_24px
            else -> R.drawable.construction_24px
        }
    }

    fun getCategoryColor(categoryName: String): Color {
        return when (getMainCategoryForSub(categoryName)) {
            "Food & Dining" -> CategoryFoodDining
            "Transportation" -> CategoryTransportation
            "Housing & Utilities" -> CategoryHouseUtilities
            "Shopping" -> CategoryShopping
            "Entertainment" -> CategoryEntertainment
            "Health & Fitness" -> CategoryHealthFitness
            "Active Income" -> CategoryActiveIncome
            "Passive Income" -> CategoryPassiveIncome
            else -> CategoryOthers
        }
    }
}