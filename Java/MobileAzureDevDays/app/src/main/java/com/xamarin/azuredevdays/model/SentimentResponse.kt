package com.xamarin.azuredevdays.model

import java.util.ArrayList

class SentimentResponse {
    var documents: List<SentimentDocument> = ArrayList()
    var errors: List<DocumentError> = ArrayList()
}
