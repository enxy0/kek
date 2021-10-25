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

void append_encode(string &, char, int);

bool assertAll();

int main() {
    cout << assertAll();
    return 0;
}

/**
 * Преобразует T в строку
 */
template<typename T>
std::string to_string(T value) {
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
            append_encode(result, last, counter);
            counter = 0;
            last = current;
        }
    }
    // проверяем счетчик (чтобы все буквы, в том числе и последняя)
    // были записаны в результирующую строку
    append_encode(result, last, counter);
    return result;
}

/**
 * Добавляет в результирующую строку закодированную букву с ее
 * количеством повторений (или просто букву без повторений)
 */
void append_encode(string &result, char letter, int counter) {
    if (counter > 1) {
        result += '(';
        result += to_string(counter);
        result += ',';
        result += letter;
        result += ')';
    } else if (counter == 1) {
        result += letter;
    }
}

/**
 * Запускает серию тестов над RLE алгоритмом.
 * Возвращает true, если все тесты пройдены.
 */
bool assertAll() {
    return run_length_encoding("abbbccddd") == "a(3,b)(2,c)(3,d)"
           && run_length_encoding("aaaaaaaa") == "(8,a)"
           && run_length_encoding("aabbccdd") == "(2,a)(2,b)(2,c)(2,d)"
           && run_length_encoding("abcd") == "abcd"
           && run_length_encoding("aaaaaaaaad") == "(9,a)d"
           && run_length_encoding("").empty();
}