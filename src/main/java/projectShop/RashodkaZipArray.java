package projectShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

/**
 * @author FedotovDV
 */
public class RashodkaZipArray {

    public static void main(String[] args) {
        String[][] arrayProduct = getProduct();
        menuShop(arrayProduct);
        yesOrNot(arrayProduct);
    }

    private static String[][] getProduct() {
        String[] partsNumber = new String[]{"256K487562", "123H54135", "802N345566", "15L253402", "15L253403"};
        String[] productName = new String[]{"Drum", "drum", "Fuser", "Feed roll", "Nudger roll"};
        String[] priceProduct = new String[]{"10000.0", "2000.0", "50000.0", "500.0", "300.0"};
        String[] quantityStock = new String[]{"2", "100", "0", "50", "30"};
        return new String[][]{partsNumber, productName, priceProduct, quantityStock};
    }

    private static String[] getSortMenu() {
        return new String[]{"Выход", "Сортировать по артикулу", "Сортировать по товару", "Сортировать по цене"};
    }

    private static String[] getShopMenu() {
        return new String[]{"Выход", "Вывести на экран весь ЗИП", "Добавить ЗИП", "Отсортировать ЗИП", "Найти ЗИП", "Купить ЗИП", "Удалить ЗИП", "Вывести меню "};
    }

    private static String[] getBuyMenu() {
        return new String[]{"Выход", "Показать корзину", "Купить товар", "Удалить из корзины"};
    }

    private static void yesOrNot(String[][] arrayProduct) {
        System.out.println("Вы хотите закончить работу? Y/N");
        while (intputStringWOExeption().equals("N")) {
            menuShop(arrayProduct);
        }
    }

    private static void menuShop(String[][] arrayProduct) {

        String[] shopMenu = getShopMenu();
        printShopMenu(shopMenu);
        System.out.println("Введите пункт меню, или 0 для выхода:");
        int itemMenu = getInputNumber();
        while (itemMenu != 0) {
            if ((itemMenu < 0) || (itemMenu > shopMenu.length - 1)) {
                System.out.print("Неправильный ввод, введите число от 0 до " + (shopMenu.length - 1) + "\n");
            }
            switch (itemMenu) {
                case 1:
                    printPriceList(arrayProduct);
                    break;
                case 2:
                    arrayProduct = addProduct(arrayProduct);
                    break;
                case 3:
                    arrayProduct = sortingProduct(arrayProduct);
                    break;
                case 4:
                    searchProduct(arrayProduct);
                    break;
                case 5:
                    buyProduct(arrayProduct);
                    break;
                case 6:
                    arrayProduct = deleteProduct(arrayProduct);
                    printPriceList(arrayProduct);
                    break;
                case 7:
                    printShopMenu(shopMenu);
                    break;
            }

            System.out.println("Для продолжения введите пункт меню, или 0 для выхода:");
            itemMenu = inputIntWOExeption();
        }
    }


    private static String[][] sortingProduct(String[][] arrayProduct) {
        String[] sortMenu = getSortMenu();
        printShopMenu(sortMenu);
        int itemSortMenu = getInputNumber();
        while (itemSortMenu != 0) {
            if ((itemSortMenu < 0) || (itemSortMenu > sortMenu.length - 1)) {
                System.out.print("Неправильный ввод, введите число от 0 до " + (sortMenu.length - 1) + "\n");
            } else {
                sortProduct(itemSortMenu - 1, arrayProduct);
                printPriceList(arrayProduct);
                break;
            }
            itemSortMenu = inputIntWOExeption();
        }
        return arrayProduct;
    }


    private static void buyProduct(String[][] arrayProduct) {
        printPriceList(arrayProduct);
        System.out.println("Выберете товар: введите номер товара или 0 для завершения. ");
        int inputIndexBasket = getInputIndex();
        int countBasket = 0;
        int[] indexBasketArray = new int[countBasket + 1];
        while (inputIndexBasket != -1) {
            if (inputIndexBasket >= 0 && inputIndexBasket < arrayProduct[0].length + 1) {

                if (parseInt(arrayProduct[3][inputIndexBasket]) > 0) {
                    if (countBasket < 1) {
                        indexBasketArray[countBasket] = inputIndexBasket;
                    } else {
                        indexBasketArray = arrayPlus(indexBasketArray);
                        indexBasketArray[countBasket] = inputIndexBasket;
                    }
                    countBasket++;
                    System.out.println("Товар добавлен в корзину.");
                } else {
                    System.out.println("Данного товара нет в наличии.");
                }
            } else {
                System.out.println("Введите корректно от 0 до " + arrayProduct[0].length);
            }

            System.out.println("Выберете следующий товар: введите номер товара или 0 для завершения. ");
            inputIndexBasket = getInputIndex();
        }
        String[] buyMenu = getBuyMenu();
        printShopMenu(buyMenu);
        System.out.println("Введите пункт меню, или 0 для выхода:");
        int itemMenu = getInputNumber();
        while (itemMenu != 0) {
            if ((itemMenu < 0) || (itemMenu > buyMenu.length - 1)) {
                System.out.print("Неправильный ввод, введите число от 0 до " + (buyMenu.length - 1) + "\n");
            }
            if (indexBasketArray == null) {
                System.out.println("Корзина пуста");
                return;
            }
            switch (itemMenu) {
                case 1:
                    printPriceList(indexBasketArray, arrayProduct);
                    break;
                case 2:
                    buyInBasketList(indexBasketArray, arrayProduct);
                    indexBasketArray = null;
                    break;
                case 3:
                    indexBasketArray = delIndexProduct(indexBasketArray, arrayProduct);
                    if (indexBasketArray.length > 0) {
                        printPriceList(indexBasketArray, arrayProduct);
                    } else {
                        System.out.println("Корзина пуста");
                        return;
                    }
                    break;
            }
            printShopMenu(buyMenu);
            System.out.println("Для продолжения введите пункт меню, или 0 для выхода:");
            itemMenu = inputIntWOExeption();
        }
    }


    private static int[] delIndexProduct(int[] indexBasketArray, String[][] arrayProduct) {
        printPriceList(indexBasketArray, arrayProduct);
        System.out.println("Введите индекс товара, который нужно удалить");
        int inputIndexBasket = getInputIndex();
        if ((inputIndexBasket < -1) || (inputIndexBasket > indexBasketArray.length - 1)) {
            System.out.print("Неправильный ввод, введите число от 1 до " + (arrayProduct.length + 1) + "\n");
            inputIndexBasket = getInputIndex();
        }
        return getTempIndex(indexBasketArray, inputIndexBasket);
    }

    private static int[] getTempIndex(int[] indexBasketArray, int inputIndexBasket) {
        int[] indexBasketArrayTemp = new int[indexBasketArray.length - 1];
        int j = 0;
        for (int value : indexBasketArray) {
            if (value != inputIndexBasket) {
                if (j == indexBasketArray.length - 1) {
                    System.out.println("Неправильно введен индекс товара");
                    return indexBasketArray;
                }
                indexBasketArrayTemp[j] = value;
                j++;
            }
        }
        return indexBasketArrayTemp;
    }

    private static int getInputNumber() {
        int number = inputIntWOExeption();
        if (number < 0) {
            System.out.println("Неправильный ввод, введите число > 0");
            number = inputIntWOExeption();
        }
        return number;
    }

    private static int getInputIndex() {
        int index = inputIntWOExeption();
        if (index < 0) {
            System.out.println("Неправильный ввод, введите число > 0");
            index = inputIntWOExeption();
        }
        return index - 1;
    }

    private static String[][] buyInBasketList(int[] indexBasketArray, String[][] arrayProduct) {

        int quantityProduct = 0;
        for (int value : indexBasketArray) {
            System.out.println("Введите количество " + arrayProduct[0][value] + " " +
                    arrayProduct[1][value] + " остаток на складе :" + arrayProduct[3][value]);

            while (quantityProduct >= 0) {
                quantityProduct = inputIntWOExeption();
                int quantityTemp = valueOf(arrayProduct[3][value]);
                if ((quantityProduct < 0) || (quantityProduct <= quantityTemp)) {
                    quantityTemp -= quantityProduct;
                    arrayProduct[3][value] = String.valueOf(quantityTemp);
                    System.out.println("Куплен товар: " + arrayProduct[0][value] + " " +
                            arrayProduct[1][value] + " " + quantityProduct + " шт.");
                    printPriceList(value, arrayProduct);
                    break;
                } else {
                    System.out.print("Неправильный ввод, введите число от 0 до " + (quantityTemp) + "\n");
                }
            }
        }
        return arrayProduct;
    }

    private static int[] arrayPlus(int[] indexBasketArray) {
        int[] tempArray = new int[indexBasketArray.length + 1];
        for (int i = 0; i < indexBasketArray.length; i++) {
            tempArray[i] = indexBasketArray[i];
        }
        indexBasketArray = new int[indexBasketArray.length + 1];
        for (int i = 0; i < indexBasketArray.length; i++) {
            indexBasketArray[i] = tempArray[i];
        }
        return indexBasketArray;
    }

    private static void searchProduct(String[][] arrayProduct) {
        System.out.println("Введите артикул товара или название: ");
        String inputProduct = intputStringWOExeption();
        int count = howManySearchProduct(inputProduct, arrayProduct);
        if (count != 0) {
            int[] index = new int[count];
            searchProduct(index, inputProduct, arrayProduct);
            printPriceList(index, arrayProduct);
        }

    }

    private static int[] searchProduct(int[] indexArray, String inputProduct, String[][] arrayProduct) {
        int j = 0;
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < arrayProduct[0].length; i++) {
                if ((arrayProduct[k][i].equalsIgnoreCase(inputProduct)) || (arrayProduct[k][i].toUpperCase().contains(inputProduct.toUpperCase()))) {
                    indexArray[j] = i;
                    j++;
                }
            }
        }
        return indexArray;
    }

    private static int howManySearchProduct(String inputProduct, String[][] arrayProduct) {
        int count = 0;
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < arrayProduct[0].length; i++) {
                if ((arrayProduct[k][i].equalsIgnoreCase(inputProduct)) || (arrayProduct[k][i].toUpperCase().contains(inputProduct.toUpperCase()))) {
                    count++;
                }
            }
        }
        if (count == 0) {
            System.out.println("Товар " + inputProduct + " не найден!");
        }
        return count;
    }

    private static String[][] sortProduct(int index, String[][] arrayProduct) {

        String[] tempArray = new String[arrayProduct.length];
        int minId;
        int k;

        for (int i = 0; i < arrayProduct[index].length; i++) {
            changeAllOfArray(arrayProduct, tempArray, i);
            minId = i;
            for (int j = i + 1; j < arrayProduct[index].length; j++) {
                if (index == 2) {
                    if (Double.parseDouble(arrayProduct[index][j]) < Double.parseDouble(tempArray[index])) {
                        changeAllOfArray(arrayProduct, tempArray, j);
                        minId = j;
                    }
                } else {
                    int minLengthId = minId;
                    String elementJ = arrayProduct[index][j].toUpperCase();
                    String elementTemp = tempArray[index].toUpperCase();
                    if ((elementJ.charAt(0) <= elementTemp.charAt(0) && (minId != j))) {
                        if (arrayProduct[index][j].charAt(0) == tempArray[index].charAt(0)) {
                            if (arrayProduct[index][j].length() < tempArray[index].length()) {
                                minLengthId = j;
                            }
                            int n = 1;
                            while ((n < arrayProduct[index][minLengthId].length()) && (arrayProduct[index][j].charAt(n) <= tempArray[index].charAt(n))) {
                                if ((arrayProduct[index][j].charAt(n) < tempArray[index].charAt(n)) && (minId != j)) {
                                    changeAllOfArray(arrayProduct, tempArray, j);
                                    minId = j;
                                }
                                n++;
                            }
                        } else {
                            changeAllOfArray(arrayProduct, tempArray, j);
                            minId = j;
                        }
                    }
                }
            }
            if (i != minId) {
                for (k = 0; k < arrayProduct.length; k++) {
                    arrayProduct[k][minId] = arrayProduct[k][i];
                    arrayProduct[k][i] = tempArray[k];
                }
            }
        }
        return arrayProduct;
    }

    private static String[] changeAllOfArray(String[][] arrayProduct, String[] tempArray, int j) {
        for (int k = 0; k < arrayProduct.length; k++) {
            tempArray[k] = arrayProduct[k][j];
        }
        return tempArray;
    }

    private static String[][] addProduct(String[][] arrayProduct) {

        System.out.println("Введите артикул товара: ");
        String productNew = inputCheckString();
        arrayProduct[0] = addProduct(productNew, arrayProduct[0]);
        System.out.println("Введите наименование товара: ");
        productNew = inputCheckString();
        arrayProduct[1] = addProduct(productNew, arrayProduct[1]);
        System.out.println("Введите цену товара:(Например: 100,50) ");
        productNew = inputCheckDouble();
        arrayProduct[2] = addProduct(productNew, arrayProduct[2]);
        System.out.println("Введите остаток на складе");
        productNew = inputCheckInt();
        arrayProduct[3] = addProduct(productNew, arrayProduct[3]);
        return new String[][]{arrayProduct[0], arrayProduct[1], arrayProduct[2], arrayProduct[3]};
    }

    private static String[][] deleteProduct(String[][] arrayProduct) {
        printPriceList(arrayProduct);
        int index = delIndexProduct(arrayProduct);
        return deleteInArray(arrayProduct, index);
    }

    private static int delIndexProduct(String[][] arrayProduct) {
        System.out.println("Введите индекс товара, который нужно удалить");
        int index = getInputIndex();
        while ((index < 0) || (index > arrayProduct.length)) {
            System.out.print("Неправильный ввод, введите число от 1 до " + (arrayProduct.length + 1) + "\n");
            index = getInputIndex();
        }
        return index;
    }


    private static String[][] deleteInArray(String[][] arrayProduct, int index) {

        String[][] deleteArrayProduct = new String[arrayProduct.length][arrayProduct[0].length - 1];
        int j = 0;
        for (int i = 0; i < arrayProduct[0].length; i++) {
            if (i != index) {
                for (int k = 0; k < arrayProduct.length; k++) {
                    deleteArrayProduct[k][j] = arrayProduct[k][i];

                }
                j++;
            }

        }
        return deleteArrayProduct;
    }

    private static String inputCheckInt() {
        int number = getInputNumber();
        while (number < 0) {
            System.out.print("Неправильный ввод, введите число >0 или 0\n");
            number = inputIntWOExeption();
        }
        return Integer.toString(number);
    }

    private static String inputCheckDouble() {
        double number = inputDoubleWOExeption();
        while (number <= 0) {
            System.out.print("Неправильный ввод, введите число >0 \n");
            number = inputDoubleWOExeption();
        }
        return Double.toString(number);
    }

    private static String inputCheckString() {

        return intputStringWOExeption();
    }

    private static String[] addProduct(String productNew, String[] Product) {
        String[] arrayNewProduct = new String[Product.length + 1];
        arrayNewProduct[Product.length] = productNew;
        for (int i = 0; i < Product.length; i++) {
            arrayNewProduct[i] = Product[i];
        }
        return arrayNewProduct;
    }

    private static void printShopMenu(String[] shopMenu) {
        System.out.println("Меню");
        for (int i = 1; i < shopMenu.length; i++) {
            System.out.println(i + ".  " + shopMenu[i] + ". ");
        }
        System.out.println("0.  " + shopMenu[0] + ". ");
    }

    private static void printPriceList(String[][] arrayProduct) {
        System.out.printf("    %-10s           %-20s              %-10s         %s\n", "Артикул", "Наименование", "Цена", "Остаток");
        for (int j = 0; j < arrayProduct[0].length; j++) {
            System.out.printf((j + 1) + "   %-10s           %-20s              %-10s         %s\n",
                    arrayProduct[0][j], arrayProduct[1][j], arrayProduct[2][j], arrayProduct[3][j]);
        }
    }

    private static void printPriceList(int[] index, String[][] arrayProduct) {
        System.out.printf("    %-10s           %-20s              %-10s         %s\n", "Артикул", "Наименование", "Цена", "Остаток");
        for (int value : index) {
            System.out.printf((value + 1) + "   %-10s           %-20s              %-10s         %s\n",
                    arrayProduct[0][value], arrayProduct[1][value], arrayProduct[2][value], arrayProduct[3][value]);
        }
    }

    private static void printPriceList(int index, String[][] arrayProduct) {
        System.out.printf("    %-10s           %-20s              %-10s         %s\n", "Артикул", "Наименование", "Цена", "Остаток");
        System.out.printf((index + 1) + "   %-10s           %-20s              %-10s         %s\n",
                arrayProduct[0][index], arrayProduct[1][index], arrayProduct[2][index], arrayProduct[3][index]);

    }

    private static int inputIntWOExeption() {
        int number = -1;
        String text;
        BufferedReader inputBR = new BufferedReader(new InputStreamReader(System.in));
        try {
            text = inputBR.readLine();
            number = parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return number;
    }

    private static double inputDoubleWOExeption() {
        double number = -1.0;
        String text;
        BufferedReader inputBR = new BufferedReader(new InputStreamReader(System.in));
        try {
            text = inputBR.readLine();
            number = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return number;
    }

    private static String intputStringWOExeption() {
        String text = "";
        BufferedReader inputBR = new BufferedReader(new InputStreamReader(System.in));
        try {
            text = inputBR.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return text;
    }

}