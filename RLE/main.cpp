/**
 * Одна из реализаций RLE (Run Length Encoding) алгоритма
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 09.10.2020
 */

#include <iostream>
#include <sstream>

using namespace std;

string run_length_encoding(string);
bool assertAll();

int main() {
    cout << assertAll();
    return 0;
}

/**
 * Преобразует T в строку
 */
template <typename T>
std::string to_string (T value )
{
    std::ostringstream ss;
    ss << value;
    return ss.str();
}

/**
 * Кодирует строку с помощью RLE алгоритма
 */
string run_length_encoding(string text) {
    if (text.length() < 1) {
        return "";
    }
    string result;
    // Заранее выделяем память, чтобы быстро происходили операции += на строках
    result.reserve(text.length());
    char last = text[0];
    int counter = 1;
    for (int i = 1; i < text.length(); ++i, ++counter) {
        char current = text[i];
        if (current != last) {
            result += last;
            result += to_string(counter);
            counter = 0;
            last = current;
        }
    }
    // проверяем счетчик (чтобы все буквы, в том числе и последняя)
    // были записаны в результирующую строку
    if (counter != 0) {
        result += last;
        result += to_string(counter);
    }
    return result;
}

/**
 * Запускает серию тестов над RLE алгоритмом.
 * Возвращает true, если все тесты пройдены.
 */
bool assertAll() {
    return run_length_encoding("abbbccddd") == "a1b3c2d3"
    && run_length_encoding("aaaaaaaa") == "a8"
    && run_length_encoding("aabbccdd") == "a2b2c2d2"
    && run_length_encoding("abcd") == "a1b1c1d1"
    && run_length_encoding("aaaaaaaaad") == "a9d1"
    && run_length_encoding("").empty();
}