import { useState, useEffect } from 'react';
import { useAccount, useConnect, useDisconnect } from 'wagmi';
import { MetaMaskConnector } from 'wagmi/connectors/metaMask';
import { readContract, writeContract } from '@wagmi/core';
import abi from '../abi/NewsPublisher.json';
import { upload } from '../lib/ipfs';
import { config } from '../wagmiConfig';

const contractAddress = process.env.NEXT_PUBLIC_CONTRACT_ADDRESS;

export default function Home() {
  const { address, isConnected } = useAccount();
  const { connect } = useConnect({ connector: new MetaMaskConnector() });
  const { disconnect } = useDisconnect();
  const [articles, setArticles] = useState([]);
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [loading, setLoading] = useState(false);

  const fetchArticles = async () => {
    try {
      const data = await readContract({
        address: contractAddress,
        abi,
        functionName: 'getAllArticles',
      });
      setArticles(data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    if (isConnected) fetchArticles();
  }, [isConnected]);

  const handlePublish = async () => {
    setLoading(true);
    try {
      const cid = await upload(body);
      await writeContract({
        address: contractAddress,
        abi,
        functionName: 'publishArticle',
        args: [title, cid],
      });
      setTitle('');
      setBody('');
      await fetchArticles();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleVote = async (id) => {
    try {
      await writeContract({
        address: contractAddress,
        abi,
        functionName: 'vote',
        args: [id],
      });
      await fetchArticles();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>News Publisher</h1>
      {!isConnected ? (
        <button onClick={() => connect()}>Connect MetaMask</button>
      ) : (
        <div>
          <p>Connected as {address}</p>
          <button onClick={disconnect}>Disconnect</button>
        </div>
      )}

      {isConnected && (
        <div style={{ marginTop: '2rem' }}>
          <h2>Publish Article</h2>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Title"
          />
          <br />
          <textarea
            value={body}
            onChange={(e) => setBody(e.target.value)}
            placeholder="Content"
          />
          <br />
          <button onClick={handlePublish} disabled={loading}>
            Submit
          </button>
        </div>
      )}

      <div style={{ marginTop: '2rem' }}>
        <h2>Articles</h2>
        <button onClick={fetchArticles}>Refresh</button>
        <ul>
          {articles.map((a) => (
            <li key={a.id.toString()} style={{ marginBottom: '1rem' }}>
              <strong>{a.title}</strong> by {a.author} - votes: {a.voteCount}
              <br />
              <ArticleContent uri={a.content} />
              <button onClick={() => handleVote(a.id)}>Vote</button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

function ArticleContent({ uri }) {
  const [content, setContent] = useState('');
  useEffect(() => {
    if (!uri) return;
    fetch(`https://ipfs.io/ipfs/${uri}`)
      .then((res) => res.text())
      .then(setContent)
      .catch((err) => console.error(err));
  }, [uri]);

  return <p>{content}</p>;
}
