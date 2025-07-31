import { WagmiConfig } from 'wagmi';
import { config } from '../wagmiConfig';
import '../styles/globals.css';

export default function App({ Component, pageProps }) {
  return (
    <WagmiConfig config={config}>
      <Component {...pageProps} />
    </WagmiConfig>
  );
}
