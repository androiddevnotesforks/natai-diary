import '../styles/globals.css'
import type {AppProps} from 'next/app'

import {QueryClient, QueryClientProvider} from '@tanstack/react-query'
import {NextIntlProvider} from "next-intl";
import {getTranslations} from "../i18n/i18n";
import {useRouter} from "next/router";
import {AppContext, useGlobalState} from "../src/modules/common/state";

const queryClient = new QueryClient()

export default function App({Component, pageProps}: AppProps) {
    const router = useRouter()
    const globalAppState = useGlobalState()
    const locale = router.locale || "en"
    const messages = getTranslations(locale)

    return (
        <NextIntlProvider messages={messages} locale={locale}>
            <QueryClientProvider client={queryClient}>
                <AppContext.Provider value={globalAppState}>
                    <Component {...pageProps} />
                </AppContext.Provider>
            </QueryClientProvider>
        </NextIntlProvider>
    )
}