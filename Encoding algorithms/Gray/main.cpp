/**
 * Перевод чисел в код Грея и обратно.
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 10.10.2020
 */

#include <iostream>
#include <bitset>

using namespace std;

unsigned int from_gray(unsigned int);

unsigned int to_gray(unsigned int);

string to_binary_string(unsigned int);

bool test();

int main() {
    bool isCorrect = test();
    cout << endl << "All tests passed: " << isCorrect << endl;
    return 0;
}

/**
 * Запускает перевод чисел в диапазоне 0..15 и проверяет перевод в код Грея и обратно.
 *
 * Формат вывода (например для 15):
 * Number: 15
 * Gray:   1000
 * Binary: 1111
 * Correct: 1
 *
 * Correct показывает результат сравнения переводов (если 1, то все верно).
 */
bool test() {
    bool allCorrect = true;
    cout << "Start testing..." << endl;
    for (unsigned int number = 0; number < 16; ++number) {
        unsigned int gray = to_gray(number);
        unsigned int binary = from_gray(gray);
        cout << "Number: " << number << endl;
        cout << "Gray:   " << to_binary_string(gray) << endl;
        cout << "Binary: " << to_binary_string(binary) << endl;
        bool isCorrect = to_binary_string(binary) == to_binary_string(from_gray(gray));
        allCorrect &= isCorrect;
        cout << "Correct: " << isCorrect << endl;
        cout << endl;
    }
    cout << "End testing!" << endl;
    return allCorrect;
}

/**
 * Преобразовывает двоичное число в код Грея
 *
 * Пример преобразования двоичного числа в код Грея:
 *  10110
 *  01011
 *  ----- XOR
 *  11101
 */
unsigned int to_gray(unsigned int n) {
    return n ^ (n >> 1);
}

/**
 * Переводит код Грея в двоичное число
 *
 * Функция осуществляет последовательный сдвиг вправо и суммирование исходного двоичного числа,
 * до тех пор, пока очередной сдвиг не обнулит слагаемое.
 *
 * Пример преобразования кода Грея в двоичное число:
 * 11101
 * 01110
 * 00111
 * 00011
 * 00001
 * -----
 * 10110
 */
unsigned int from_gray(unsigned int gray) {
    unsigned int bin;
    for (bin = 0; gray; gray >>= 1) {
        bin ^= gray;
    }
    return bin;
}

/**
 * Строковое представление числа n в двоичной системе исчисления
 * Для простоты отображается только 4 бита (т.к. тесты идут на числах от 0 до 15)
 */
string to_binary_string(unsigned int n) {
    return bitset<4>(n).to_string();
}